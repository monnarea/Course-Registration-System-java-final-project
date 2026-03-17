package org.system.controller;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.poi.ss.usermodel.*;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private final Map<String, List<Map<String, String>>> userEnrollmentData = new HashMap<>();

    private static final String DB_URL      = "jdbc:postgresql://localhost:5432/student";
    private static final String DB_USER     = "postgres";
    private static final String DB_PASSWORD = "128028";

    private static final String COURSE_QUERY = """
            SELECT c.course_id, c.course_name, c.price, c.description, c.credit_score,
                   c.capacity, c.start_date, c.end_date, c.instructor_id, c.room,
                   c.major_id, c.level,
                   ct.day_of_week, ct.morning, ct.afternoon, ct.evening
            FROM course c
            LEFT JOIN LATERAL (
                SELECT day_of_week, morning, afternoon, evening
                FROM course_time
                WHERE course_id = c.course_id
                ORDER BY time_id
                LIMIT 1
            ) ct ON true
            ORDER BY c.course_id
            """;

    private static final String COURSE_LIST_QUERY = """
            SELECT c.course_id, c.course_name, c.price, c.start_date, c.end_date, c.room,
                   ct.morning, ct.afternoon, ct.evening
            FROM course c
            LEFT JOIN LATERAL (
                SELECT morning, afternoon, evening
                FROM course_time
                WHERE course_id = c.course_id
                ORDER BY time_id
                LIMIT 1
            ) ct ON true
            ORDER BY c.course_id
            """;

    // ── Button label constants (used in consume() switch and keyboard builder) ─
    private static final String BTN_ENROLL      = "📚 Enroll in a Course";
    private static final String BTN_SCHEDULE    = "📅 Get Schedule";
    private static final String BTN_ENROLLMENTS = "🎓 My Enrollments";
    private static final String BTN_HELP        = "❓ Help";

    public TelegramBotController(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    // ── Build persistent bottom reply keyboard ────────────────────────────────
    private ReplyKeyboardMarkup buildMainReplyKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(BTN_ENROLL));
        row1.add(new KeyboardButton(BTN_SCHEDULE));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(BTN_ENROLLMENTS));
        row2.add(new KeyboardButton(BTN_HELP));

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .resizeKeyboard(true)
                .isPersistent(true)
                .build();
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
            return;
        }
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String text           = update.getMessage().getText();
        String chatId         = String.valueOf(update.getMessage().getChatId());
        String userId         = String.valueOf(update.getMessage().getFrom().getId());
        boolean isPrivate     = update.getMessage().getChatId() > 0;
        String normalizedText = text.split("@")[0].toLowerCase().trim();

        // Handle free-text input states first (enrollment flow)
        // All enrollment steps are now handled via inline button callbacks

        switch (normalizedText) {
            // Commands
            case "/start"  -> handleStart(chatId, userId, update, isPrivate);
            case "/hello"  -> handleHello(chatId, update);
            case "/clear"  -> clearBotMessages(chatId);
            case "/cancel" -> handleCancel(chatId, userId);

            // Board keyboard buttons (lowercase match)
            case "📚 enroll in a course" -> handleEnroll(chatId, userId, isPrivate);
            case "📅 get schedule"       -> handleGetSchedule(chatId, userId, isPrivate);
            case "🎓 my enrollments"     -> handleMyEnrollments(chatId, userId);
            case "❓ help"               -> handleHelp(chatId);

            default -> { if (isPrivate) sendMessage(chatId, "Please use /start or the menu buttons below."); }
        }
    }

    // ── /start ────────────────────────────────────────────────────────────────
    private void handleStart(String chatId, String userId, Update update, boolean isPrivate) {
        String name = update.getMessage().getFrom().getUserName();
        name = (name != null) ? "@" + name : update.getMessage().getFrom().getFirstName();

        userState.remove(userId);
        userSelectedCourse.remove(userId);

        try {
            var sent = telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("👋 Welcome, " + name + "!\n\nWhat would you like to do?\nUse the buttons below ⬇️")
                    .replyMarkup(buildMainReplyKeyboard())
                    .build());
            trackMessage(chatId, sent.getMessageId());
        } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    // ── Callbacks (inline buttons — kept for future use) ──────────────────────
    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String chatId       = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        String userId       = String.valueOf(update.getCallbackQuery().getFrom().getId());
        boolean isPrivate   = update.getCallbackQuery().getMessage().getChatId() > 0;

        try { telegramClient.execute(AnswerCallbackQuery.builder().callbackQueryId(update.getCallbackQuery().getId()).build()); }
        catch (TelegramApiException e) { e.printStackTrace(); }

        if (callbackData.startsWith("course:")) {
            String courseName = callbackData.substring("course:".length());
            handleCourseSelected(chatId, userId, courseName);
        } else if (callbackData.startsWith("shift:")) {
            String shift = callbackData.substring("shift:".length());
            handleShiftSelected(chatId, userId, shift);
        } else {
            switch (callbackData) {
                case "action:enroll"         -> handleEnroll(chatId, userId, isPrivate);
                case "action:get_schedule"   -> handleGetSchedule(chatId, userId, isPrivate);
                case "action:my_enrollments" -> handleMyEnrollments(chatId, userId);
                case "action:help"           -> handleHelp(chatId);
                default                      -> sendMessage(chatId, "Unknown action.");
            }
        }
    }

    // ── Enroll flow ───────────────────────────────────────────────────────────
    private void handleEnroll(String chatId, String userId, boolean isPrivate) {
        String target = isPrivate ? chatId : userId;

        if (!isPrivate) {
            userGroupOrigin.put(userId, chatId);
            sendMessage(chatId, "I've sent you a private message to complete your enrollment!\nPlease check your DM with me to continue.");
        }

        // Send each course as its own card
        List<String> cards = buildCourseCards();
        for (String card : cards) {
            sendMarkdownMessage(target, card);
        }

        // Build inline buttons — one per course name
        List<String> courseNames = fetchCourseNames();
        if (courseNames.isEmpty()) {
            sendMessage(target, "No courses available at the moment.");
            return;
        }

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();
        for (String name : courseNames) {
            keyboardBuilder.keyboardRow(new InlineKeyboardRow(
                    InlineKeyboardButton.builder()
                            .text(name)
                            .callbackData("course:" + name)
                            .build()
            ));
        }

        try {
            var sent = telegramClient.execute(SendMessage.builder()
                    .chatId(target)
                    .text("*Step 1:* Select a course to enroll in:")
                    .parseMode("Markdown")
                    .replyMarkup(keyboardBuilder.build())
                    .build());
            trackMessage(target, sent.getMessageId());
        } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private List<String> fetchCourseNames() {
        List<String> names = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT course_name FROM course ORDER BY course_id");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) names.add(rs.getString("course_name"));
        } catch (Exception e) { e.printStackTrace(); }
        return names;
    }

    private void handleCourseSelected(String chatId, String userId, String courseName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(COURSE_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                if (!rs.getString("course_name").equalsIgnoreCase(courseName)) continue;

                String morning   = nullToDash(rs.getString("morning"));
                String afternoon = nullToDash(rs.getString("afternoon"));
                String evening   = nullToDash(rs.getString("evening"));

                userSelectedCourse.put(userId, courseName);

                // Build shift inline buttons for only available shifts
                InlineKeyboardMarkup.InlineKeyboardMarkupBuilder kb = InlineKeyboardMarkup.builder();
                InlineKeyboardRow shiftRow = new InlineKeyboardRow();
                if (!morning.equals("-"))   shiftRow.add(InlineKeyboardButton.builder().text("Morning ("   + morning   + ")").callbackData("shift:morning").build());
                if (!afternoon.equals("-")) shiftRow.add(InlineKeyboardButton.builder().text("Afternoon (" + afternoon + ")").callbackData("shift:afternoon").build());
                if (!evening.equals("-"))   shiftRow.add(InlineKeyboardButton.builder().text("Evening ("   + evening   + ")").callbackData("shift:evening").build());

                if (shiftRow.isEmpty()) {
                    sendMessage(chatId, "No shifts are currently available for " + courseName + ". Please choose another course.");
                    return;
                }
                kb.keyboardRow(shiftRow);

                try {
                    var sent = telegramClient.execute(SendMessage.builder()
                            .chatId(chatId)
                            .text("*" + courseName + "* selected.\n\n*Step 2:* Choose your preferred shift:")
                            .parseMode("Markdown")
                            .replyMarkup(kb.build())
                            .build());
                    trackMessage(chatId, sent.getMessageId());
                } catch (TelegramApiException e) { e.printStackTrace(); }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "Something went wrong. Please try again.");
        }
    }



    private void handleShiftSelected(String chatId, String userId, String shift) {
        String courseName = userSelectedCourse.get(userId);
        if (courseName == null) {
            sendMessage(chatId, "Something went wrong. Please use /start to try again.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(COURSE_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                if (!rs.getString("course_name").equalsIgnoreCase(courseName)) continue;

                String shiftTime = switch (shift) {
                    case "morning"   -> rs.getString("morning");
                    case "afternoon" -> rs.getString("afternoon");
                    default          -> rs.getString("evening");
                };

                if (shiftTime == null || shiftTime.equalsIgnoreCase("Null")) {
                    sendMarkdownMessage(chatId, "The *" + shift + "* shift is not available for *" + courseName + "*.\n\nPlease choose another shift or type /cancel to go back.");
                    return;
                }

                String shiftLabel = shift.substring(0, 1).toUpperCase() + shift.substring(1);

                Map<String, String> data = new HashMap<>();
                data.put("courseId",    rs.getString("course_id"));
                data.put("courseName",  courseName);
                data.put("price",       rs.getString("price"));
                data.put("description", rs.getString("description"));
                data.put("creditScore", rs.getString("credit_score"));
                data.put("capacity",    rs.getString("capacity"));
                data.put("startDate",   rs.getString("start_date") != null ? rs.getString("start_date") : "N/A");
                data.put("endDate",     rs.getString("end_date")   != null ? rs.getString("end_date")   : "N/A");
                data.put("room",        rs.getString("room"));
                data.put("majorId",     rs.getString("major_id"));
                data.put("level",       rs.getString("level"));
                data.put("dayOfWeek",   rs.getString("day_of_week"));
                data.put("shift",       shiftLabel + " (" + shiftTime + ")");
                userEnrollmentData.computeIfAbsent(userId, k -> new ArrayList<>()).add(data);

                String summary = "*" + courseName + "*\n" +
                        "Course ID: " + data.get("courseId") + "\n" +
                        "Price: $" + data.get("price") + "\n" +
                        "Date: " + data.get("startDate") + " - " + data.get("endDate") + "\n" +
                        "Day: " + data.get("dayOfWeek") + "\n" +
                        "Room: " + data.get("room") + "\n" +
                        "Shift: " + shiftLabel + " (" + shiftTime + ")";

                userEnrollments.computeIfAbsent(userId, k -> new ArrayList<>()).add(summary);
                userState.remove(userId);
                userSelectedCourse.remove(userId);
                sendMarkdownMessage(chatId, "*Enrollment Successful!* ✅\n\n" + summary + "\n\nGood luck with your studies!\n ---->>> Tap [Get Schedule] below to view or download your full schedule.");

                String groupChatId = userGroupOrigin.remove(userId);
                if (groupChatId != null) sendMarkdownMessage(groupChatId, "A member has successfully enrolled in *" + courseName + "* (" + shiftLabel + " shift)!");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "Something went wrong. Please try again later.");
        }

        userState.remove(userId);
        userSelectedCourse.remove(userId);
    }

    // ── Get Schedule ──────────────────────────────────────────────────────────
    private void handleGetSchedule(String chatId, String userId, boolean isPrivate) {
        List<Map<String, String>> enrollments = userEnrollmentData.getOrDefault(userId, new ArrayList<>());
        if (enrollments.isEmpty()) {
            sendMarkdownMessage(chatId, "📅 *Get Schedule*\n\nYou have no enrollments yet.\nPlease enroll in a course first using the *📚 Enroll in a Course* button.");
            return;
        }
        if (isPrivate) {
            sendSchedule(chatId, userId);
        } else {
            sendMessage(chatId, "📨 I've sent your schedule to your private DM!\nPlease check your messages with me.");
            sendSchedule(userId, userId);
        }
    }

    private void sendSchedule(String chatId, String userId) {
        List<Map<String, String>> enrollments = userEnrollmentData.getOrDefault(userId, new ArrayList<>());
        StringBuilder sb = new StringBuilder("📅 *Your Schedule:*\n\n");
        for (int i = 0; i < enrollments.size(); i++) {
            Map<String, String> d = enrollments.get(i);
            sb.append(i + 1).append(". 📘 *").append(d.get("courseName")).append("*\n")
                    .append("   ->> Course ID: ").append(d.get("courseId")).append("\n")
                    .append("   ->> Price: $").append(d.get("price")).append("\n")
                    .append("   ->> ").append(d.get("description")).append("\n")
                    .append("   ->> Credit Score: ").append(d.get("creditScore")).append("\n")
                    .append("   ->> Capacity: ").append(d.get("capacity")).append("\n")
                    .append("   ->> ").append(d.get("startDate")).append(" → ").append(d.get("endDate")).append("\n")
                    .append("   ->> Room: ").append(d.get("room")).append("\n")
                    .append("   ->> Major ID: ").append(d.get("majorId")).append("\n")
                    .append("   ->> Level: ").append(d.get("level")).append("\n")
                    .append("   ->> Day: ").append(d.get("dayOfWeek")).append("\n")
                    .append("   ->> Shift: ").append(d.get("shift")).append("\n")
                    .append("────────────────────────────────\n");
        }
        sb.append("\n📎 Your schedule PDF file is below ⬇️");
        sendMarkdownMessage(chatId, sb.toString());

        File pdfFile = generateSchedulePDF(userId, enrollments);
        if (pdfFile != null) { sendPdfFile(chatId, pdfFile, "My_Schedule.pdf"); pdfFile.delete(); }
        else sendMessage(chatId, "❌ Could not generate PDF file. Please try again later.");
    }

    // ── PDF Generation ────────────────────────────────────────────────────────
    private File generateSchedulePDF(String userId, List<Map<String, String>> enrollments) {
        try {
            PDDocument document = new PDDocument();
            PDRectangle landscape = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
            PDPage page = new PDPage(landscape);
            document.addPage(page);

            float pageWidth    = landscape.getWidth();
            float pageHeight   = landscape.getHeight();
            float margin       = 30f;
            float contentWidth = pageWidth - 2 * margin;

            PDFont fontBold    = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDFont fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            float fontSize       = 8.5f;
            float headerFontSize = 9.5f;
            float titleFontSize  = 22f;
            float lineSpacing    = fontSize + 3f;
            float cellPadding    = 5f;

            String[] colHeaders = {
                    "No.", "Course ID", "Course Name", "Price ($)", "Description",
                    "Credit", "Cap.", "Start Date", "End Date", "Room", "Major ID", "Level", "Shift"
            };
            float[] colWeights = {3, 7, 13, 7, 18, 5, 4, 9, 9, 7, 7, 5, 12};

            float totalWeight = 0;
            for (float w : colWeights) totalWeight += w;
            float[] colWidths = new float[colWeights.length];
            for (int i = 0; i < colWeights.length; i++)
                colWidths[i] = (colWeights[i] / totalWeight) * contentWidth;

            float headerHeight = 30f;
            float titleHeight  = 46f;
            float yStart       = pageHeight - margin;

            PDPageContentStream cs = new PDPageContentStream(document, page);

            // Title banner
            cs.setNonStrokingColor(33 / 255f, 90 / 255f, 160 / 255f);
            cs.addRect(margin, yStart - titleHeight, contentWidth, titleHeight);
            cs.fill();
            cs.beginText();
            cs.setFont(fontBold, titleFontSize);
            cs.setNonStrokingColor(1f, 1f, 1f);
            float titleW = fontBold.getStringWidth("MY COURSE SCHEDULE") / 1000f * titleFontSize;
            cs.newLineAtOffset(margin + (contentWidth - titleW) / 2, yStart - titleHeight + 12);
            cs.showText("MY COURSE SCHEDULE");
            cs.endText();

            float y = yStart - titleHeight;

            cs = drawHeaderRow(cs, fontBold, colHeaders, colWidths,
                    margin, y, headerHeight, contentWidth, headerFontSize);
            y -= headerHeight;

            for (int rowIdx = 0; rowIdx < enrollments.size(); rowIdx++) {
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

                List<List<String>> wrappedCells = new ArrayList<>();
                int maxLines = 1;
                for (int col = 0; col < values.length; col++) {
                    List<String> lines = wrapText(values[col], fontRegular, fontSize, colWidths[col] - 2 * cellPadding);
                    wrappedCells.add(lines);
                    if (lines.size() > maxLines) maxLines = lines.size();
                }

                float rowHeight = maxLines * lineSpacing + 2 * cellPadding;

                // Page break if needed
                if (y - rowHeight < margin + 20) {
                    cs.setStrokingColor(0.2f, 0.4f, 0.7f);
                    cs.setLineWidth(1.5f);
                    cs.addRect(margin, y, contentWidth, (pageHeight - margin - titleHeight) - y);
                    cs.stroke();

                    cs.beginText();
                    cs.setFont(fontRegular, 8);
                    cs.setNonStrokingColor(0.5f, 0.5f, 0.5f);
                    cs.newLineAtOffset(margin, margin / 2);
                    cs.showText("Generated by CS Student Bot  |  Total Courses: " + enrollments.size() + "  |  (continued...)");
                    cs.endText();
                    cs.close();

                    page = new PDPage(landscape);
                    document.addPage(page);
                    cs = new PDPageContentStream(document, page);
                    y = pageHeight - margin;

                    cs = drawHeaderRow(cs, fontBold, colHeaders, colWidths,
                            margin, y, headerHeight, contentWidth, headerFontSize);
                    y -= headerHeight;
                }

                // Alternating row background
                float[] bg = (rowIdx % 2 == 0)
                        ? new float[]{0.93f, 0.96f, 1f}
                        : new float[]{1f, 1f, 1f};
                cs.setNonStrokingColor(bg[0], bg[1], bg[2]);
                cs.addRect(margin, y - rowHeight, contentWidth, rowHeight);
                cs.fill();

                // Cell text (multi-line)
                float xPos = margin;
                cs.setNonStrokingColor(0.1f, 0.1f, 0.1f);
                for (int col = 0; col < values.length; col++) {
                    List<String> lines = wrappedCells.get(col);
                    for (int ln = 0; ln < lines.size(); ln++) {
                        float textY = y - cellPadding - fontSize - (ln * lineSpacing);
                        cs.beginText();
                        cs.setFont(fontRegular, fontSize);
                        cs.newLineAtOffset(xPos + cellPadding, textY);
                        cs.showText(lines.get(ln));
                        cs.endText();
                    }
                    xPos += colWidths[col];
                }

                // Row grid lines
                cs.setStrokingColor(0.8f, 0.8f, 0.85f);
                cs.setLineWidth(0.4f);
                cs.moveTo(margin, y - rowHeight);
                cs.lineTo(margin + contentWidth, y - rowHeight);
                cs.stroke();
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

            // Outer border
            cs.setStrokingColor(0.2f, 0.4f, 0.7f);
            cs.setLineWidth(1.5f);
            cs.addRect(margin, y, contentWidth, (pageHeight - margin - titleHeight) - y);
            cs.stroke();

            // Footer
            cs.beginText();
            cs.setFont(fontRegular, 8);
            cs.setNonStrokingColor(0.5f, 0.5f, 0.5f);
            cs.newLineAtOffset(margin, margin / 2);
            cs.showText("Generated by CS Student Bot  |  Total Courses: " + enrollments.size());
            cs.endText();
            cs.close();

            File tempFile = File.createTempFile("schedule_" + userId + "_", ".pdf");
            tempFile.delete();
            document.save(tempFile);
            document.close();
            return tempFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ── Draw header row ───────────────────────────────────────────────────────
    private PDPageContentStream drawHeaderRow(
            PDPageContentStream cs,
            PDFont fontBold,
            String[] colHeaders,
            float[] colWidths,
            float margin,
            float y,
            float headerHeight,
            float contentWidth,
            float headerFontSize) throws IOException {

        cs.setNonStrokingColor(52 / 255f, 120 / 255f, 210 / 255f);
        cs.addRect(margin, y - headerHeight, contentWidth, headerHeight);
        cs.fill();

        float xPos = margin;
        cs.setNonStrokingColor(1f, 1f, 1f);
        for (int i = 0; i < colHeaders.length; i++) {
            float tw = fontBold.getStringWidth(colHeaders[i]) / 1000f * headerFontSize;
            cs.beginText();
            cs.setFont(fontBold, headerFontSize);
            cs.newLineAtOffset(xPos + (colWidths[i] - tw) / 2, y - headerHeight + 10);
            cs.showText(colHeaders[i]);
            cs.endText();
            xPos += colWidths[i];
        }

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

        return cs;
    }

    // ── Word-wrap ─────────────────────────────────────────────────────────────
    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) { lines.add(""); return lines; }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            float testWidth = font.getStringWidth(testLine) / 1000f * fontSize;
            if (testWidth > maxWidth && currentLine.length() > 0) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }
        if (currentLine.length() > 0) lines.add(currentLine.toString());
        return lines;
    }

    private String safe(String val) {
        return (val == null || val.isBlank()) ? "N/A" : val;
    }

    private String nullToDash(String val) {
        return (val == null || val.equalsIgnoreCase("Null") || val.isBlank()) ? "-" : val;
    }

    // ── Send PDF ──────────────────────────────────────────────────────────────
    private void sendPdfFile(String chatId, File file, String fileName) {
        try {
            telegramClient.execute(SendDocument.builder().chatId(chatId)
                    .document(new InputFile(file, fileName))
                    .caption("📋 Here is your course schedule as a PDF file!").build());
        } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    // ── Build course list as a list of individual course cards ────────────────
    private List<String> buildCourseCards() {
        List<String> cards = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(COURSE_LIST_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String morning   = nullToDash(rs.getString("morning"));
                String afternoon = nullToDash(rs.getString("afternoon"));
                String evening   = nullToDash(rs.getString("evening"));

                String card =
                        "*" + rs.getString("course_id") + ". " + rs.getString("course_name") + "*\n" +
                                "┌─────────────────────────\n" +
                                "│ Price     : `$" + rs.getString("price") + "`\n" +
                                "│ Room      : `" + rs.getString("room") + "`\n" +
                                "│ Start     : `" + rs.getString("start_date") + "`\n" +
                                "│ End       : `" + rs.getString("end_date") + "`\n" +
                                "│ Morning   : `" + morning + "`\n" +
                                "│ Afternoon : `" + afternoon + "`\n" +
                                "│ Evening   : `" + evening + "`\n" +
                                "└─────────────────────────";
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
            cards.add("❌ Could not load courses. Please try again later.");
        }
        return cards;
    }

    // ── My Enrollments ────────────────────────────────────────────────────────
    private void handleMyEnrollments(String chatId, String userId) {
        List<String> enrollments = userEnrollments.getOrDefault(userId, new ArrayList<>());
        if (enrollments.isEmpty()) {
            sendMessage(chatId, "🎓 You have no enrollments yet.\n\nTap 📚 Enroll in a Course to get started.");
            return;
        }
        StringBuilder sb = new StringBuilder("🎓 *My Enrollments:*\n\n");
        for (int i = 0; i < enrollments.size(); i++)
            sb.append(i + 1).append(". ").append(enrollments.get(i)).append("\n─────────────────\n");
        sendMarkdownMessage(chatId, sb.toString());
    }

    // ── Help ──────────────────────────────────────────────────────────────────
    private void handleHelp(String chatId) {
        sendMarkdownMessage(chatId,
                "❓ *Help*\n\n" +
                        "Use the buttons at the bottom of the screen:\n\n" +
                        "📚 *Enroll in a Course* — Browse and enroll in available courses\n" +
                        "📅 *Get Schedule* — View your schedule and download as PDF\n" +
                        "🎓 *My Enrollments* — See all your enrolled courses\n" +
                        "❓ *Help* — Show this help message\n\n" +
                        "*Commands:*\n" +
                        "• /start — Show welcome message & keyboard\n" +
                        "• /cancel — Cancel current action\n" +
                        "• /hello — Greet the bot\n" +
                        "• /clear — Delete bot messages");
    }

    // ── Hello ─────────────────────────────────────────────────────────────────
    private void handleHello(String chatId, Update update) {
        String name = update.getMessage().getFrom().getUserName();
        name = (name != null) ? "@" + name : update.getMessage().getFrom().getFirstName();
        sendMessage(chatId, "Hello " + name + "! 👋");
    }

    // ── Cancel ────────────────────────────────────────────────────────────────
    private void handleCancel(String chatId, String userId) {
        userGroupOrigin.remove(userId);
        userSelectedCourse.remove(userId);
        if (userState.containsKey(userId)) {
            userState.remove(userId);
            sendMessage(chatId, "❌ Action cancelled. Use the menu buttons below to continue.");
        } else {
            sendMessage(chatId, "Nothing to cancel. Use the menu buttons below.");
        }
    }

    // ── Clear messages ────────────────────────────────────────────────────────
    private void clearBotMessages(String chatId) {
        List<Integer> ids = botMessageIds.getOrDefault(chatId, new ArrayList<>());
        if (ids.isEmpty()) { sendMessage(chatId, "No messages to clear."); return; }
        for (Integer id : new ArrayList<>(ids)) {
            try { telegramClient.execute(new DeleteMessage(chatId, id)); } catch (TelegramApiException e) { e.printStackTrace(); }
        }
        ids.clear();
        sendMessage(chatId, "✅ Bot messages cleared!");
    }

    // ── Messaging helpers ─────────────────────────────────────────────────────
    private void sendMessage(String chatId, String text) {
        try {
            var sent = telegramClient.execute(SendMessage.builder().chatId(chatId).text(text).build());
            trackMessage(chatId, sent.getMessageId());
        } catch (TelegramApiException e) {
            System.err.println("⚠️ Could not send message to " + chatId + ". User may need to start the bot privately first.");
            e.printStackTrace();
        }
    }

    private void sendMarkdownMessage(String chatId, String text) {
        try {
            var sent = telegramClient.execute(SendMessage.builder().chatId(chatId).text(text).parseMode("Markdown").build());
            trackMessage(chatId, sent.getMessageId());
        } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void trackMessage(String chatId, int messageId) {
        botMessageIds.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageId);
    }

    // ── Utility (kept for compatibility) ──────────────────────────────────────
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
}