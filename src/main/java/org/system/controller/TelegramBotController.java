package org.system.controller;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;    
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramBotController implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final Map<String, List<Integer>> botMessageIds = new HashMap<>();

    private final Map<String, String> userState = new HashMap<>();
    private final Map<String, List<String>> userEnrollments = new HashMap<>();
    private final Map<String, String> userSelectedCourse = new HashMap<>();
    private final Map<String, String> userGroupOrigin = new HashMap<>();

    // Stores full enrollment data for PDF export
    private final Map<String, List<Map<String, String>>> userEnrollmentData = new HashMap<>();

    public TelegramBotController(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
            return;
        }

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text       = update.getMessage().getText();
        String chatId     = String.valueOf(update.getMessage().getChatId());
        String userId     = String.valueOf(update.getMessage().getFrom().getId());
        boolean isPrivate = update.getMessage().getChatId() > 0;

        String normalizedText = text.split("@")[0].toLowerCase().trim();

        // Intercept plain-text for multi-step flows (private only)
        if (isPrivate && !normalizedText.startsWith("/")) {
            String state = userState.get(userId);
            if ("awaiting_shift".equals(state)) {
                handleShiftInput(chatId, userId, text.trim());
                return;
            }
            if ("awaiting_course_name".equals(state)) {
                handleEnrollmentInput(chatId, userId, text.trim());
                return;
            }
        }

        switch (normalizedText) {
            case "/start"  -> handleStart(chatId, userId, update, isPrivate);
            case "/hello"  -> handleHello(chatId, update);
            case "/clear"  -> clearBotMessages(chatId);
            case "/cancel" -> handleCancel(chatId, userId);
            default        -> {
                if (isPrivate) {
                    sendMessage(chatId, "Please use /start to see available options.");
                }
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // /start  →  main menu
    // ──────────────────────────────────────────────────────────────────────────
    private void handleStart(String chatId, String userId, Update update, boolean isPrivate) {
        String name = update.getMessage().getFrom().getUserName();
        name = (name != null) ? "@" + name : update.getMessage().getFrom().getFirstName();

        userState.remove(userId);
        userSelectedCourse.remove(userId);

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text("📚 Enroll in a Course")
                                .callbackData("action:enroll")
                                .build()
                ))
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text("📅 Get Schedule")
                                .callbackData("action:get_schedule")
                                .build()
                ))
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text("🎓 My Enrollments")
                                .callbackData("action:my_enrollments")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("❓ Help")
                                .callbackData("action:help")
                                .build()
                ))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("👋 Welcome, " + name + "!\n\nWhat would you like to do?")
                .replyMarkup(keyboard)
                .build();

        try {
            var sent = telegramClient.execute(message);
            trackMessage(chatId, sent.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Callbacks
    // ──────────────────────────────────────────────────────────────────────────
    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String chatId       = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        String userId       = String.valueOf(update.getCallbackQuery().getFrom().getId());
        boolean isPrivate   = update.getCallbackQuery().getMessage().getChatId() > 0;

        try {
            telegramClient.execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        switch (callbackData) {
            case "action:enroll"         -> handleEnroll(chatId, userId, isPrivate);
            case "action:get_schedule"   -> handleGetSchedule(chatId, userId, isPrivate);
            case "action:my_enrollments" -> handleMyEnrollments(chatId, userId);
            case "action:help"           -> handleHelp(chatId);
            default                      -> sendMessage(chatId, "Unknown action.");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ENROLL FLOW
    // ──────────────────────────────────────────────────────────────────────────
    private void handleEnroll(String chatId, String userId, boolean isPrivate) {
        String courseList = buildCourseList();

        if (isPrivate) {
            userState.put(userId, "awaiting_course_name");
            sendMarkdownMessage(chatId,
                    courseList + "\n" +
                            "📚 *Step 1:* Please type the *course name* you want to enroll in:\n" +
                            "Type /cancel to cancel."
            );
        } else {
            userGroupOrigin.put(userId, chatId);
            userState.put(userId, "awaiting_course_name");
            sendMessage(chatId,
                    "📨 I've sent you a private message to complete your enrollment!\n" +
                            "Please check your DM with me to continue."
            );
            sendMarkdownMessage(userId,
                    courseList + "\n" +
                            "📚 *Step 1:* Please type the *course name* you want to enroll in:\n" +
                            "Type /cancel to cancel.\n\n_(This was triggered from the group chat)_"
            );
        }
    }

    // Step 1: validate course name → show available shifts
    private void handleEnrollmentInput(String chatId, String userId, String input) {
        try (FileInputStream fis = new FileInputStream("Course_Output.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean found = false;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String courseName = getCellValue(row.getCell(1));

                if (courseName.equalsIgnoreCase(input)) {
                    found = true;

                    String morning   = getCellValue(row.getCell(13));
                    String afternoon = getCellValue(row.getCell(14));
                    String evening   = getCellValue(row.getCell(15));

                    StringBuilder shiftMsg = new StringBuilder("✅ Course found!\n\n📋 *Available Shifts:*\n");
                    if (!morning.equalsIgnoreCase("Null") && !morning.equalsIgnoreCase("N/A"))
                        shiftMsg.append("🌅 Morning: ").append(morning).append("\n");
                    if (!afternoon.equalsIgnoreCase("Null") && !afternoon.equalsIgnoreCase("N/A"))
                        shiftMsg.append("☀️ Afternoon: ").append(afternoon).append("\n");
                    if (!evening.equalsIgnoreCase("Null") && !evening.equalsIgnoreCase("N/A"))
                        shiftMsg.append("🌙 Evening: ").append(evening).append("\n");

                    shiftMsg.append("\n📚 *Step 2:* Please type your preferred shift:\n")
                            .append("`morning` / `afternoon` / `evening`\n")
                            .append("Type /cancel to cancel.");

                    userSelectedCourse.put(userId, courseName);
                    userState.put(userId, "awaiting_shift");
                    sendMarkdownMessage(chatId, shiftMsg.toString());
                    break;
                }
            }

            if (!found) {
                sendMarkdownMessage(chatId,
                        "⚠️ Course *\"" + input + "\"* not found.\n\n" +
                                "Please check the name and try again.\n" +
                                "Type /cancel to go back to the menu."
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
            userState.remove(userId);
            userGroupOrigin.remove(userId);
            sendMessage(chatId, "❌ Something went wrong. Please try again later.");
        }
    }

    // Step 2: validate shift → finalize enrollment + save full data
    private void handleShiftInput(String chatId, String userId, String input) {
        String shift = input.toLowerCase().trim();

        if (!shift.equals("morning") && !shift.equals("afternoon") && !shift.equals("evening")) {
            sendMarkdownMessage(chatId,
                    "⚠️ Invalid shift. Please type one of:\n" +
                            "`morning` / `afternoon` / `evening`\n" +
                            "Type /cancel to cancel."
            );
            return;
        }

        String courseName = userSelectedCourse.get(userId);
        if (courseName == null) {
            sendMessage(chatId, "❌ Something went wrong. Please use /start to try again.");
            userState.remove(userId);
            return;
        }

        try (FileInputStream fis = new FileInputStream("Course_Output.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                if (!getCellValue(row.getCell(1)).equalsIgnoreCase(courseName)) continue;

                String courseId    = getCellValue(row.getCell(0));
                String price       = getCellValue(row.getCell(2));
                String description = getCellValue(row.getCell(3));
                String creditScore = getCellValue(row.getCell(4));
                String capacity    = getCellValue(row.getCell(5));
                String startDate   = getCellValue(row.getCell(6));
                String endDate     = getCellValue(row.getCell(7));
                String room        = getCellValue(row.getCell(9));
                String majorId     = getCellValue(row.getCell(10));
                String level       = getCellValue(row.getCell(11));
                String dayOfWeek   = getCellValue(row.getCell(12));

                String shiftTime;
                String shiftEmoji;
                switch (shift) {
                    case "morning"   -> { shiftTime = getCellValue(row.getCell(13)); shiftEmoji = "🌅"; }
                    case "afternoon" -> { shiftTime = getCellValue(row.getCell(14)); shiftEmoji = "☀️"; }
                    default          -> { shiftTime = getCellValue(row.getCell(15)); shiftEmoji = "🌙"; }
                }

                if (shiftTime.equalsIgnoreCase("Null") || shiftTime.equalsIgnoreCase("N/A")) {
                    sendMarkdownMessage(chatId,
                            "⚠️ The *" + shift + "* shift is not available for *" + courseName + "*.\n\n" +
                                    "Please choose another shift or type /cancel to go back."
                    );
                    return;
                }

                String shiftLabel = shift.substring(0, 1).toUpperCase() + shift.substring(1);

                // Save full enrollment data for PDF export
                Map<String, String> data = new HashMap<>();
                data.put("courseId",    courseId);
                data.put("courseName",  courseName);
                data.put("price",       price);
                data.put("description", description);
                data.put("creditScore", creditScore);
                data.put("capacity",    capacity);
                data.put("startDate",   startDate);
                data.put("endDate",     endDate);
                data.put("room",        room);
                data.put("majorId",     majorId);
                data.put("level",       level);
                data.put("dayOfWeek",   dayOfWeek);
                data.put("shift",       shiftLabel + " (" + shiftTime + ")");
                userEnrollmentData.computeIfAbsent(userId, k -> new ArrayList<>()).add(data);

                String enrollmentSummary =
                        "📘 *" + courseName + "*\n" +
                                "🆔 Course ID: " + courseId + "\n" +
                                "💰 Price: $" + price + "\n" +
                                "📅 " + startDate + " → " + endDate + "\n" +
                                "📆 Day: " + dayOfWeek + "\n" +
                                "🚪 Room: " + room + "\n" +
                                shiftEmoji + " Shift: " + shiftLabel + " (" + shiftTime + ")";

                userEnrollments.computeIfAbsent(userId, k -> new ArrayList<>()).add(enrollmentSummary);
                userState.remove(userId);
                userSelectedCourse.remove(userId);

                sendMarkdownMessage(chatId,
                        "✅ *Enrollment Successful!*\n\n" +
                                enrollmentSummary + "\n\n" +
                                "Good luck with your studies! 🎓\n" +
                                "Use /start to go back to the menu."
                );

                String groupChatId = userGroupOrigin.remove(userId);
                if (groupChatId != null) {
                    sendMarkdownMessage(groupChatId,
                            "✅ A member has successfully enrolled in *" + courseName +
                                    "* (" + shiftLabel + " shift)!"
                    );
                }
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(chatId, "❌ Something went wrong. Please try again later.");
        }

        userState.remove(userId);
        userSelectedCourse.remove(userId);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // GET SCHEDULE FLOW
    // ──────────────────────────────────────────────────────────────────────────
    private void handleGetSchedule(String chatId, String userId, boolean isPrivate) {
        List<Map<String, String>> enrollments = userEnrollmentData.getOrDefault(userId, new ArrayList<>());

        if (enrollments.isEmpty()) {
            sendMarkdownMessage(chatId,
                    "📅 *Get Schedule*\n\n" +
                            "You have no enrollments yet.\n" +
                            "Please enroll in a course first using *📚 Enroll in a Course*.\n\n" +
                            "Use /start to go back."
            );
            return;
        }

        if (isPrivate) {
            sendSchedule(chatId, userId);
        } else {
            sendMessage(chatId,
                    "📨 I've sent your schedule to your private DM!\n" +
                            "Please check your messages with me."
            );
            sendSchedule(userId, userId);
        }
    }

    private void sendSchedule(String chatId, String userId) {
        List<Map<String, String>> enrollments = userEnrollmentData.getOrDefault(userId, new ArrayList<>());

        // 1. Send text summary
        StringBuilder sb = new StringBuilder("📅 *Your Schedule:*\n\n");
        for (int i = 0; i < enrollments.size(); i++) {
            Map<String, String> d = enrollments.get(i);
            sb.append(i + 1).append(". 📘 *").append(d.get("courseName")).append("*\n")
                    .append("   🆔 Course ID: ").append(d.get("courseId")).append("\n")
                    .append("   💰 Price: $").append(d.get("price")).append("\n")
                    .append("   📝 ").append(d.get("description")).append("\n")
                    .append("   🎯 Credit Score: ").append(d.get("creditScore")).append("\n")
                    .append("   👥 Capacity: ").append(d.get("capacity")).append("\n")
                    .append("   📅 ").append(d.get("startDate")).append(" → ").append(d.get("endDate")).append("\n")
                    .append("   🚪 Room: ").append(d.get("room")).append("\n")
                    .append("   🏫 Major ID: ").append(d.get("majorId")).append("\n")
                    .append("   📊 Level: ").append(d.get("level")).append("\n")
                    .append("   📆 Day: ").append(d.get("dayOfWeek")).append("\n")
                    .append("   ⏰ Shift: ").append(d.get("shift")).append("\n")
                    .append("─────────────────\n");
        }
        sb.append("\n📎 Your schedule PDF file is below ⬇️");
        sendMarkdownMessage(chatId, sb.toString());

        // 2. Generate and send PDF
        File pdfFile = generateSchedulePDF(userId, enrollments);
        if (pdfFile != null) {
            sendPdfFile(chatId, pdfFile, "My_Schedule.pdf");
            pdfFile.delete();
        } else {
            sendMessage(chatId, "❌ Could not generate PDF file. Please try again later.");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Generate PDF schedule using Apache PDFBox
    // ──────────────────────────────────────────────────────────────────────────
    private File generateSchedulePDF(String userId, List<Map<String, String>> enrollments) {
        try {
            PDDocument document = new PDDocument();

            // Use A4 landscape for the wide table
            PDRectangle landscape = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
            PDPage page = new PDPage(landscape);
            document.addPage(page);

            float pageWidth  = landscape.getWidth();   // ~842
            float pageHeight = landscape.getHeight();  // ~595
            float margin     = 30f;
            float contentWidth = pageWidth - 2 * margin;

            // Fonts
            PDFont fontBold    = PDType1Font.HELVETICA_BOLD;
            PDFont fontRegular = PDType1Font.HELVETICA;
            PDFont fontTitle   = PDType1Font.HELVETICA_BOLD;

            // Column definitions: label + relative weight
            String[] colHeaders = {
                    "No.", "Course ID", "Course Name", "Price ($)",
                    "Description", "Credit", "Cap.",
                    "Start Date", "End Date", "Room",
                    "Major ID", "Level", "Shift"
            };
            float[] colWeights = {3, 7, 13, 7, 18, 5, 4, 9, 9, 7, 7, 5, 12};

            float totalWeight = 0;
            for (float w : colWeights) totalWeight += w;

            float[] colWidths = new float[colWeights.length];
            for (int i = 0; i < colWeights.length; i++) {
                colWidths[i] = (colWeights[i] / totalWeight) * contentWidth;
            }

            float rowHeight    = 22f;
            float headerHeight = 26f;
            float titleHeight  = 36f;
            float yStart       = pageHeight - margin;

            PDPageContentStream cs = new PDPageContentStream(document, page);

            // ── Background header banner ─────────────────────────────────────
            cs.setNonStrokingColor(33 / 255f, 90 / 255f, 160 / 255f);
            cs.addRect(margin, yStart - titleHeight, contentWidth, titleHeight);
            cs.fill();

            // ── Title text ───────────────────────────────────────────────────
            cs.beginText();
            cs.setFont(fontTitle, 16);
            cs.setNonStrokingColor(1f, 1f, 1f); // white
            float titleTextWidth = fontTitle.getStringWidth("MY COURSE SCHEDULE") / 1000f * 16;
            cs.newLineAtOffset(margin + (contentWidth - titleTextWidth) / 2, yStart - titleHeight + 10);
            cs.showText("MY COURSE SCHEDULE");
            cs.endText();

            float y = yStart - titleHeight;

            // ── Column header row ─────────────────────────────────────────────
            cs.setNonStrokingColor(52 / 255f, 120 / 255f, 210 / 255f);
            cs.addRect(margin, y - headerHeight, contentWidth, headerHeight);
            cs.fill();

            float xPos = margin;
            cs.setNonStrokingColor(1f, 1f, 1f);
            for (int i = 0; i < colHeaders.length; i++) {
                String headerText = colHeaders[i];
                float fontSize = 7.5f;
                float textWidth = fontBold.getStringWidth(headerText) / 1000f * fontSize;
                float textX = xPos + (colWidths[i] - textWidth) / 2;
                cs.beginText();
                cs.setFont(fontBold, fontSize);
                cs.newLineAtOffset(textX, y - headerHeight + 8);
                cs.showText(headerText);
                cs.endText();
                xPos += colWidths[i];
            }

            // Draw header border lines
            cs.setStrokingColor(0.7f, 0.7f, 0.9f);
            cs.setLineWidth(0.5f);
            xPos = margin;
            for (float w : colWidths) {
                cs.moveTo(xPos, y);
                cs.lineTo(xPos, y - headerHeight);
                cs.stroke();
                xPos += w;
            }
            cs.moveTo(xPos, y);
            cs.lineTo(xPos, y - headerHeight);
            cs.stroke();

            y -= headerHeight;

            // ── Data rows ────────────────────────────────────────────────────
            for (int rowIdx = 0; rowIdx < enrollments.size(); rowIdx++) {
                // Check if new page needed
                if (y - rowHeight < margin + 20) {
                    cs.close();
                    page = new PDPage(landscape);
                    document.addPage(page);
                    cs = new PDPageContentStream(document, page);
                    y = pageHeight - margin;

                    // Repeat header on new page
                    cs.setNonStrokingColor(52 / 255f, 120 / 255f, 210 / 255f);
                    cs.addRect(margin, y - headerHeight, contentWidth, headerHeight);
                    cs.fill();

                    xPos = margin;
                    cs.setNonStrokingColor(1f, 1f, 1f);
                    for (int i = 0; i < colHeaders.length; i++) {
                        float fontSize = 7.5f;
                        float textWidth = fontBold.getStringWidth(colHeaders[i]) / 1000f * fontSize;
                        float textX = xPos + (colWidths[i] - textWidth) / 2;
                        cs.beginText();
                        cs.setFont(fontBold, fontSize);
                        cs.newLineAtOffset(textX, y - headerHeight + 8);
                        cs.showText(colHeaders[i]);
                        cs.endText();
                        xPos += colWidths[i];
                    }
                    y -= headerHeight;
                }

                Map<String, String> d = enrollments.get(rowIdx);
                String[] values = {
                        String.valueOf(rowIdx + 1),
                        safe(d.get("courseId")),
                        safe(d.get("courseName")),
                        "$" + safe(d.get("price")),
                        safe(d.get("description")),
                        safe(d.get("creditScore")),
                        safe(d.get("capacity")),
                        safe(d.get("startDate")),
                        safe(d.get("endDate")),
                        safe(d.get("room")),
                        safe(d.get("majorId")),
                        safe(d.get("level")),
                        safe(d.get("shift"))
                };

                // Alternating row background
                if (rowIdx % 2 == 0) {
                    cs.setNonStrokingColor(0.93f, 0.96f, 1f); // light blue
                } else {
                    cs.setNonStrokingColor(1f, 1f, 1f); // white
                }
                cs.addRect(margin, y - rowHeight, contentWidth, rowHeight);
                cs.fill();

                // Row text
                xPos = margin;
                cs.setNonStrokingColor(0.1f, 0.1f, 0.1f);
                for (int col = 0; col < values.length; col++) {
                    float fontSize = 7f;
                    // Truncate text if too wide for column
                    String cellText = truncateText(values[col], fontRegular, fontSize, colWidths[col] - 4);
                    cs.beginText();
                    cs.setFont(fontRegular, fontSize);
                    cs.newLineAtOffset(xPos + 2, y - rowHeight + 7);
                    cs.showText(cellText);
                    cs.endText();
                    xPos += colWidths[col];
                }

                // Row grid lines
                cs.setStrokingColor(0.8f, 0.8f, 0.85f);
                cs.setLineWidth(0.4f);
                // Horizontal line below row
                cs.moveTo(margin, y - rowHeight);
                cs.lineTo(margin + contentWidth, y - rowHeight);
                cs.stroke();
                // Vertical column dividers
                xPos = margin;
                for (float w : colWidths) {
                    cs.moveTo(xPos, y);
                    cs.lineTo(xPos, y - rowHeight);
                    cs.stroke();
                    xPos += w;
                }
                cs.moveTo(xPos, y);
                cs.lineTo(xPos, y - rowHeight);
                cs.stroke();

                y -= rowHeight;
            }

            // ── Outer border ─────────────────────────────────────────────────
            float tableTop    = pageHeight - margin - titleHeight;
            float tableBottom = y;
            cs.setStrokingColor(0.2f, 0.4f, 0.7f);
            cs.setLineWidth(1.5f);
            cs.addRect(margin, tableBottom, contentWidth, tableTop - tableBottom);
            cs.stroke();

            // ── Footer ───────────────────────────────────────────────────────
            cs.beginText();
            cs.setFont(fontRegular, 7);
            cs.setNonStrokingColor(0.5f, 0.5f, 0.5f);
            cs.newLineAtOffset(margin, margin / 2);
            cs.showText("Generated by CS Student Bot  |  Total Courses: " + enrollments.size());
            cs.endText();

            cs.close();

            // Save to temp file
            File tempFile = File.createTempFile("schedule_" + userId + "_", ".pdf");
            document.save(tempFile);
            document.close();
            return tempFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Truncate text so it fits inside a column width
    private String truncateText(String text, PDFont font, float fontSize, float maxWidth) {
        try {
            if (font.getStringWidth(text) / 1000f * fontSize <= maxWidth) return text;
            while (text.length() > 1) {
                text = text.substring(0, text.length() - 1);
                if (font.getStringWidth(text + "...") / 1000f * fontSize <= maxWidth)
                    return text + "...";
            }
        } catch (IOException e) {
            // ignore
        }
        return text;
    }

    private String safe(String val) {
        return (val == null || val.isBlank()) ? "N/A" : val;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Send PDF file as document via Telegram
    // ──────────────────────────────────────────────────────────────────────────
    private void sendPdfFile(String chatId, File file, String fileName) {
        try {
            SendDocument sendDocument = SendDocument.builder()
                    .chatId(chatId)
                    .document(new InputFile(file, fileName))
                    .caption("📋 Here is your course schedule as a PDF file!")
                    .build();
            telegramClient.execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Build course list (shown during enroll flow)
    // ──────────────────────────────────────────────────────────────────────────
    private String buildCourseList() {
        try (FileInputStream fis = new FileInputStream("Course_Output.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            StringBuilder sb = new StringBuilder("🗂 *Available Courses:*\n\n");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String courseId    = getCellValue(row.getCell(0));
                String course      = getCellValue(row.getCell(1));
                String price       = getCellValue(row.getCell(2));
                String description = getCellValue(row.getCell(3));
                String startDate   = getCellValue(row.getCell(6));
                String endDate     = getCellValue(row.getCell(7));
                String room        = getCellValue(row.getCell(9));
                String dayOfWeek   = getCellValue(row.getCell(12));
                String morning     = getCellValue(row.getCell(13));
                String afternoon   = getCellValue(row.getCell(14));
                String evening     = getCellValue(row.getCell(15));

                sb.append("📘 *").append(course).append("*\n")
                        .append("🆔 Course ID: ").append(courseId).append("\n")
                        .append("💰 Price: $").append(price).append("\n")
                        .append("📝 ").append(description).append("\n")
                        .append("📅 ").append(startDate).append(" → ").append(endDate).append("\n")
                        .append("📆 Day: ").append(dayOfWeek).append("\n")
                        .append("🚪 Room: ").append(room).append("\n");

                if (!morning.equalsIgnoreCase("Null") && !morning.equalsIgnoreCase("N/A"))
                    sb.append("🌅 Morning: ").append(morning).append("\n");
                if (!afternoon.equalsIgnoreCase("Null") && !afternoon.equalsIgnoreCase("N/A"))
                    sb.append("☀️ Afternoon: ").append(afternoon).append("\n");
                if (!evening.equalsIgnoreCase("Null") && !evening.equalsIgnoreCase("N/A"))
                    sb.append("🌙 Evening: ").append(evening).append("\n");

                sb.append("─────────────────\n");
            }

            sb.append("\n");
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "❌ Could not load courses.\n\n";
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // My Enrollments
    // ──────────────────────────────────────────────────────────────────────────
    private void handleMyEnrollments(String chatId, String userId) {
        List<String> enrollments = userEnrollments.getOrDefault(userId, new ArrayList<>());

        if (enrollments.isEmpty()) {
            sendMessage(chatId, "🎓 You have no enrollments yet.\n\nUse /start to go back.");
            return;
        }

        StringBuilder sb = new StringBuilder("🎓 *My Enrollments:*\n\n");
        for (int i = 0; i < enrollments.size(); i++) {
            sb.append(i + 1).append(". ").append(enrollments.get(i)).append("\n");
            sb.append("─────────────────\n");
        }
        sb.append("\nUse /start to go back.");
        sendMarkdownMessage(chatId, sb.toString());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Other handlers
    // ──────────────────────────────────────────────────────────────────────────
    private void handleHelp(String chatId) {
        sendMarkdownMessage(chatId,
                "❓ *Help*\n\n" +
                        "• /start — show the main menu\n" +
                        "• /cancel — cancel current action\n" +
                        "• /hello — greet the bot\n" +
                        "• /clear — delete all bot messages in this chat\n\n" +
                        "*Features:*\n" +
                        "• 📚 Enroll in a Course — browse and enroll\n" +
                        "• 📅 Get Schedule — view & download your schedule as PDF\n" +
                        "• 🎓 My Enrollments — view enrolled courses"
        );
    }

    private void handleHello(String chatId, Update update) {
        String name = update.getMessage().getFrom().getUserName();
        name = (name != null) ? "@" + name : update.getMessage().getFrom().getFirstName();
        sendMessage(chatId, "Hello " + name + "!");
    }

    private void handleCancel(String chatId, String userId) {
        userGroupOrigin.remove(userId);
        userSelectedCourse.remove(userId);
        if (userState.containsKey(userId)) {
            userState.remove(userId);
            sendMessage(chatId, "❌ Action cancelled. Use /start to go back to the menu.");
        } else {
            sendMessage(chatId, "Nothing to cancel. Use /start to see the menu.");
        }
    }

    private void clearBotMessages(String chatId) {
        List<Integer> ids = botMessageIds.getOrDefault(chatId, new ArrayList<>());
        if (ids.isEmpty()) {
            sendMessage(chatId, "No messages to clear.");
            return;
        }
        for (Integer messageId : new ArrayList<>(ids)) {
            try {
                telegramClient.execute(new DeleteMessage(chatId, messageId));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        ids.clear();
        sendMessage(chatId, "✅ Bot messages cleared!");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────────
    private String getCellValue(Cell cell) {
        if (cell == null) return "N/A";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toLocalDate().toString()
                    : String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> "N/A";
        };
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            var sent = telegramClient.execute(message);
            trackMessage(chatId, sent.getMessageId());
        } catch (TelegramApiException e) {
            System.err.println("⚠️ Could not send message to " + chatId +
                    ". User may need to start the bot privately first.");
            e.printStackTrace();
        }
    }

    private void sendMarkdownMessage(String chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .build();
        try {
            var sent = telegramClient.execute(message);
            trackMessage(chatId, sent.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void trackMessage(String chatId, int messageId) {
        botMessageIds.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageId);
    }
}