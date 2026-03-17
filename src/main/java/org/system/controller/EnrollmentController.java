package org.system.controller;

import org.system.exception.EnrollmentException;
import org.system.model.dto.request.EnrollmentRequestDto;
import org.system.service.EnrollmentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final Scanner scanner;

    public EnrollmentController() {
        this.enrollmentService = new EnrollmentService();
        this.scanner = new Scanner(System.in);
    }

    // ── MAIN MENU ─────────────────────────────────────────────────────────────
    public void showMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║       ENROLLMENT SYSTEM          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Enroll a Student             ║");
            System.out.println("║  2. View All Enrollments         ║");
            System.out.println("║  3. Find Enrollment by ID        ║");
            System.out.println("║  4. Find Enrollments by Student  ║");
            System.out.println("║  5. Update Enrollment            ║");
            System.out.println("║  6. Delete Enrollment            ║");
            System.out.println("║  7. Get Telegram Bot Link        ║");
            System.out.println("║  0. Exit                         ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> enrollStudent();
                case 2 -> viewAllEnrollments();
                case 3 -> findById();
                case 4 -> findByStudentId();
                case 5 -> updateEnrollment();
                case 6 -> deleteEnrollment();
                case 7 -> showTelegramLink();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // ── 1. ENROLL STUDENT ─────────────────────────────────────────────────────
    private void enrollStudent() {
        System.out.println("\n--- Enroll a Student ---");

        System.out.print("Enter Student ID   : ");
        int studentId = readInt();

        System.out.print("Enter Course ID    : ");
        int courseId = readInt();

        System.out.print("Enter Payment Method (e.g. CASH / CARD / BANK_TRANSFER): ");
        String paymentMethod = scanner.nextLine().trim();

        EnrollmentRequestDto request = EnrollmentRequestDto.builder()
                .student_id(studentId)
                .course_id(courseId)
                .enrollment_date(LocalDate.now())

                .build();

        try {
            EnrollmentRequestDto result = enrollmentService.enrollStudent(request);
            System.out.println("\n✅ Enrollment successful!");
            printEnrollment(result);

            // Show Telegram bot link after successful enrollment
            System.out.println("\n📲 Stay updated via our Telegram Bot:");
            System.out.println("   👉 " + enrollmentService.getTelegramBotLink());

        } catch (EnrollmentException e) {
            System.out.println("❌ Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 2. VIEW ALL ───────────────────────────────────────────────────────────
    private void viewAllEnrollments() {
        System.out.println("\n--- All Enrollments ---");
        try {
            List<EnrollmentRequestDto> list = enrollmentService.getAllEnrollments();
            printTable(list);
        } catch (EnrollmentException e) {
            System.out.println("❌ Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 3. FIND BY ID ─────────────────────────────────────────────────────────
    private void findById() {
        System.out.print("\nEnter Enrollment ID: ");
        int id = readInt();
        try {
            EnrollmentRequestDto enrollment = enrollmentService.getEnrollmentById(id);
            printEnrollment(enrollment);
        } catch (EnrollmentException e) {
            System.out.println("❌ Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 4. FIND BY STUDENT ID ─────────────────────────────────────────────────
    private void findByStudentId() {
        System.out.print("\nEnter Student ID: ");
        int studentId = readInt();
        try {
            List<EnrollmentRequestDto> list = enrollmentService.getEnrollmentsByStudentId(studentId);
            printTable(list);
        } catch (EnrollmentException e) {
            System.out.println("❌ Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 5. UPDATE ─────────────────────────────────────────────────────────────
    private void updateEnrollment() {
        System.out.println("\n--- Update Enrollment ---");

        System.out.print("Enter Enrollment ID to update: ");
        int enrollmentId = readInt();

        System.out.print("Enter new Course ID          : ");
        int courseId = readInt();

        System.out.print("Enter new Student ID         : ");
        int studentId = readInt();

        System.out.print("Enter new Payment Method     : ");
        String paymentMethod = scanner.nextLine().trim();

        System.out.print("Enter new Status (PENDING / APPROVED / REJECTED): ");
        String status = scanner.nextLine().trim();

        EnrollmentRequestDto request = EnrollmentRequestDto.builder()
                .enrollment_id(enrollmentId)
                .course_id(courseId)
                .student_id(studentId)
                .enrollment_date(LocalDate.now())
                .build();

        try {
            EnrollmentRequestDto updated = enrollmentService.updateEnrollment(request);
            System.out.println("\n✅ Enrollment updated successfully!");
            printEnrollment(updated);
        } catch (EnrollmentException e) {
            System.out.println("❌ Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 6. DELETE ─────────────────────────────────────────────────────────────
    private void deleteEnrollment() {
        System.out.print("\nEnter Enrollment ID to delete: ");
        int id = readInt();
        try {
            enrollmentService.deleteEnrollment(id);
            System.out.println("✅ Enrollment with ID " + id + " deleted successfully.");
        } catch (EnrollmentException e) {
            System.out.println("❌ Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 7. TELEGRAM LINK ──────────────────────────────────────────────────────
    private void showTelegramLink() {
        System.out.println("\n📲 Join our Telegram Bot for updates:");
        System.out.println("   👉 " + enrollmentService.getTelegramBotLink());
    }

    // ── PRINT TABLE ───────────────────────────────────────────────────────────
    private void printTable(List<EnrollmentRequestDto> list) {
        System.out.println("\n┌────────────┬───────────┬────────────┬─────────────┬──────────────────┬────────────┐");
        System.out.printf ("│ %-10s │ %-9s │ %-10s │ %-11s │ %-16s │ %-10s │%n",
                "Enroll ID", "Course ID", "Student ID", "Enroll Date", "Payment Method", "Status");
        System.out.println("├────────────┼───────────┼────────────┼─────────────┼──────────────────┼────────────┤");

        for (EnrollmentRequestDto e : list) {
            System.out.printf("│ %-10s │ %-9s │ %-10s │ %-11s │ %-16s │ %-10s │%n",
                    e.getEnrollment_id(),
                    e.getCourse_id(),
                    e.getStudent_id(),
                    e.getEnrollment_date() != null ? e.getEnrollment_date() : "N/A"

            );
        }

        System.out.println("└────────────┴───────────┴────────────┴─────────────┴──────────────────┴────────────┘");
        System.out.println("Total records: " + list.size());
    }

    // ── PRINT SINGLE ENROLLMENT ───────────────────────────────────────────────
    private void printEnrollment(EnrollmentRequestDto e) {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│         Enrollment Details           │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.printf ("│  Enrollment ID  : %-18s │%n", e.getEnrollment_id());
        System.out.printf ("│  Course ID      : %-18s │%n", e.getCourse_id());
        System.out.printf ("│  Student ID     : %-18s │%n", e.getStudent_id());
        System.out.printf ("│  Enroll Date    : %-18s │%n", e.getEnrollment_date() != null ? e.getEnrollment_date() : "N/A");
        System.out.println("└──────────────────────────────────────┘");
    }

    // ── HELPER: safe int read ─────────────────────────────────────────────────
    private int readInt() {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}