package org.system.controller;

import org.system.model.dto.response.TranscriptResponseDto;
import org.system.service.TranscriptService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class TranscriptController {

    private final TranscriptService transcriptService = new TranscriptService();
    private final Scanner scanner = new Scanner(System.in);
    public static final String green  = "\u001B[32m";
    public static final String blue   = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red    = "\u001B[31m";
    public static final String cyan   = "\u001B[36m";
    public static final String white  = "\u001B[37m";

    // ── CREATE ───────────────────────────────────────────────────────────────
    public void create() {
        try {
            System.out.println(cyan + "\n─── Create New Transcript ───");

            Integer studentId = readInt(yellow + "  Student ID        : ");
            if (studentId == null) return;

            Integer courseId = readInt(yellow + "  Course  ID        : ");
            if (courseId == null) return;

            LocalDateTime generatedAt = LocalDateTime.now();

            System.out.print("  Grade (e.g. A, A-, B+)              : ");
            String grade = scanner.nextLine().trim();
            if (grade.isEmpty()) { System.err.println(red + "Grade cannot be empty."); return; }

            Double gradePoint = readDouble(yellow + "  Grade Point       : ");
            if (gradePoint == null) return;

            String resultStatus = "";
            boolean valid = false;
            while (!valid) {
                System.out.print(yellow + "  Result Status (Pass/Fail)           : ");
                resultStatus = scanner.nextLine().trim();
                if (resultStatus.equalsIgnoreCase("Pass") || resultStatus.equalsIgnoreCase("Fail")) {
                    valid = true;
                } else {
                    System.out.println(red + "  Invalid! Please enter Pass or Fail.");
                }
                if (resultStatus.isEmpty()) { System.err.println(red + "Result status cannot be empty."); return; }
            }

            System.out.print(yellow + "  Completion Date (yyyy-MM-dd, blank = none): ");
            String compDateRaw = scanner.nextLine().trim();
            LocalDate completionDate = null;
            if (!compDateRaw.isEmpty()) {
                try {
                    completionDate = LocalDate.parse(compDateRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e) {
                    System.err.println(red + "  Invalid date format. Use yyyy-MM-dd");
                    return;
                }
            }

            System.out.print(yellow + "  Remarks (optional)                  : ");
            String remarks = scanner.nextLine().trim();
            if (remarks.isEmpty()) remarks = null;

            TranscriptResponseDto dto = new TranscriptResponseDto();
            dto.setStudentId(studentId);
            dto.setCourseId(courseId);
            dto.setGeneratedAt(LocalDate.from(generatedAt));
            dto.setGrade(grade);
            dto.setGrandePoint(gradePoint);
            dto.setResultStatus(resultStatus);
            dto.setCompletionDate(completionDate);
            dto.setRemarks(remarks);

            // service handles printing + insert
            boolean success = transcriptService.createTranscript(dto);
            if (success) {
                System.out.println(green + "  Transcript created successfully.");
            } else {
                System.err.println(red + "  Failed to create transcript.");
            }

        } catch (Exception e) {
            System.err.println(red + "Unexpected error in create: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── READ ALL ─────────────────────────────────────────────────────────────
    public void displayAll() {
        try {
            List<TranscriptResponseDto> list = transcriptService.getAllTranscripts();
            if (list == null || list.isEmpty()) {
                System.out.println(red+"No transcripts found.");
            }
            // printing is handled inside service
        } catch (Exception e) {
            System.err.println(red+"Unexpected error in displayAll: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── READ BY ID ───────────────────────────────────────────────────────────
    public void displayById() {
        System.out.print(yellow + "  Enter Transcript ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        try {
            List<TranscriptResponseDto> list = transcriptService.getTranscriptById(id);
            if (list == null || list.isEmpty()) {
                System.out.println("  ℹ  No transcript found for ID: " + id);
            }
            // printing is handled inside service
        } catch (Exception e) {
            System.err.println("  ✖  Unexpected error in displayById: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    public void update() {
        while (true) {
            try {
                System.out.print(yellow + "  Enter transcript ID to update: ");
                Integer transcriptId = Integer.parseInt(scanner.nextLine());

                // fetch + print current record (printing handled in service)
                List<TranscriptResponseDto> existing = transcriptService.getTranscriptById(transcriptId);
                if (existing == null || existing.isEmpty()) {
                    System.out.println(red + "  No transcript found with ID: " + transcriptId);
                    return;
                }
                TranscriptResponseDto current = existing.get(0);

                System.out.println("\n  Press Enter to keep the current value, or type a new one:\n");

                try {
                    System.out.print("  Student ID       [" + current.getStudentId() + "]: ");
                    String studentIdInput = scanner.nextLine().trim();
                    int studentId = studentIdInput.isEmpty()
                            ? current.getStudentId()
                            : Integer.parseInt(studentIdInput);

                    System.out.print("  Course  ID       [" + current.getCourseId() + "]: ");
                    String courseIdInput = scanner.nextLine().trim();
                    int courseId = courseIdInput.isEmpty()
                            ? current.getCourseId()
                            : Integer.parseInt(courseIdInput);

                    System.out.print("  Generated At     [" + current.getGeneratedAt() + "] (yyyy-MM-dd HH:mm): ");
                    String genAtInput = scanner.nextLine().trim();
                    LocalDateTime generatedAt = genAtInput.isEmpty()
                            ? current.getGeneratedAt().atStartOfDay()
                            : LocalDateTime.parse(genAtInput);

                    System.out.print("  Grade            [" + current.getGrade() + "]: ");
                    String gradeInput = scanner.nextLine().trim();
                    String grade = gradeInput.isEmpty() ? current.getGrade() : gradeInput;

                    System.out.print("  Grade Point      [" + current.getGrandePoint() + "]: ");
                    String gradePointInput = scanner.nextLine().trim();
                    double gradePoint = gradePointInput.isEmpty()
                            ? current.getGrandePoint()
                            : Double.parseDouble(gradePointInput);

                    System.out.print("  Result Status    [" + current.getResultStatus() + "]: ");
                    String resultStatusInput = scanner.nextLine().trim();
                    String resultStatus = resultStatusInput.isEmpty()
                            ? current.getResultStatus()
                            : resultStatusInput;

                    System.out.print("  Completion Date  [" + current.getCompletionDate() + "] (yyyy-MM-dd): ");
                    String compDateInput = scanner.nextLine().trim();
                    LocalDate completionDate = compDateInput.isEmpty()
                            ? current.getCompletionDate()
                            : LocalDate.parse(compDateInput);

                    System.out.print("  Remarks          [" + current.getRemarks() + "]: ");
                    String remarksInput = scanner.nextLine().trim();
                    String remarks = remarksInput.isEmpty() ? current.getRemarks() : remarksInput;

                    TranscriptResponseDto dto = new TranscriptResponseDto();
                    dto.setTranscriptId(transcriptId);
                    dto.setStudentId(studentId);
                    dto.setCourseId(courseId);
                    dto.setGeneratedAt(LocalDate.from(generatedAt));
                    dto.setGrade(grade);
                    dto.setGrandePoint(gradePoint);
                    dto.setResultStatus(resultStatus);
                    dto.setCompletionDate(completionDate);
                    dto.setRemarks(remarks);

                    // service handles printing + update
                    if (transcriptService.updateTranscript(dto)) {
                        System.out.println(green + "\n  ✔  Transcript updated successfully!");
                    } else {
                        System.err.println("  ✖  Failed to update transcript.");
                    }

                } catch (NumberFormatException e) {
                    System.err.println("  ✖  Invalid number format: " + e.getMessage());
                } catch (DateTimeParseException e) {
                    System.err.println("  ✖  Invalid date format: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("  ✖  update() failed: " + e.getMessage());
                    e.printStackTrace();
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println(red + "  Invalid input! Please enter a number.");
            }
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    public void delete() {
        System.out.print(yellow + "  Enter Transcript ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean success = transcriptService.deleteTranscript(id);
        if (success) {
            System.out.println(green + "  Transcript deleted. ID: " + id);
        } else {
            System.err.println(red + "  Failed to delete transcript. ID: " + id);
        }
    }

    // ── MENU ─────────────────────────────────────────────────────────────────
//    public void showMenu() {
//        boolean running = true;
//        while (running) {
//            System.out.println("\n===== TRANSCRIPT MANAGEMENT =====");
//            System.out.println("1. View All Transcripts");
//            System.out.println("2. View Transcript By ID");
//            System.out.println("3. Delete Transcript");
//            System.out.println("0. Exit");
//            System.out.print("Choose: ");
//
//            String choice = scanner.nextLine().trim();
//            switch (choice) {
//                case "1" -> displayAll();
//                case "2" -> displayById();
//                case "3" -> delete();
//                case "0" -> running = false;
//                default  -> System.out.println("  Invalid option. Try again.");
//            }
//        }
//    }

    // ── HELPERS ──────────────────────────────────────────────────────────────
    private Integer readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number. Please enter a valid integer.");
            return null;
        }
    }

    private Double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number. Please enter a valid decimal (e.g. 3.50).");
            return null;
        }
    }

    // ── Format ───────────────────────────────────────────────────────────
    public void format(int id) {
        try {
            List<TranscriptResponseDto> list = transcriptService.getTranscriptById(id);
            if (list == null || list.isEmpty()) {
                System.out.println("No transcript found for ID: " + id);
            }
            // printing is handled inside service
        } catch (Exception e) {
            System.err.println("Unexpected error in displayById: " + e.getMessage());
            e.printStackTrace();
        }
    }
}