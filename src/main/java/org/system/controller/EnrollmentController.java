package org.system.controller;

import org.system.exception.EnrollmentException;
import org.system.model.dto.request.EnrollmentRequestDto;
import org.system.service.EnrollmentService;

import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printEnrollmentTable;
import static org.system.view.View.printEnrollmentTablePaginated;
import org.system.util.Pagination;

public class EnrollmentController {

    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final Scanner           scanner           = new Scanner(System.in);

    public static final String green  = "\u001B[32m";
    public static final String blue   = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red    = "\u001B[31m";
    public static final String cyan   = "\u001B[36m";
    public static final String white  = "\u001B[37m";
    public static final String reset  = "\u001B[0m";

    // ── 1. CREATE ─────────────────────────────────────────────────────────────
    public void createEnrollment() {
        try {
            System.out.print(yellow + "Enter Student ID  : ");
            long studentId = Long.parseLong(scanner.nextLine().trim());

            System.out.print(yellow + "Enter Course ID   : ");
            int courseId = Integer.parseInt(scanner.nextLine().trim());

            EnrollmentRequestDto request = EnrollmentRequestDto.builder()
                    .student_id(studentId)
                    .course_id(courseId)
                    .build();

            EnrollmentRequestDto created = enrollmentService.insertEnrollment(request);
            System.out.println(green + "\n✔ Enrollment created successfully!");
            printEnrollmentTable(List.of(created));

        } catch (NumberFormatException e) {
            System.out.println(red + "Invalid input! Please enter a number.");
        } catch (EnrollmentException e) {
            System.out.println(red + "Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 2. VIEW ALL ───────────────────────────────────────────────────────────
    public void viewAllEnrollments() {
        try {
            List<EnrollmentRequestDto> list = enrollmentService.getAllEnrollments();
            printEnrollmentTablePaginated(list);
        } catch (EnrollmentException e) {
            System.out.println(red + "Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 3. FIND BY ID ─────────────────────────────────────────────────────────
    public void findById() {
        try {
            System.out.print(yellow + "Enter Enrollment ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            EnrollmentRequestDto enrollment = enrollmentService.getEnrollmentById(id);
            printEnrollmentTable(List.of(enrollment));

        } catch (NumberFormatException e) {
            System.out.println(red + "Invalid input! Please enter a number.");
        } catch (EnrollmentException e) {
            System.out.println(red + "Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 4. FIND BY STUDENT ID ─────────────────────────────────────────────────
    public void findByStudentId() {
        try {
            System.out.print(yellow + "Enter Student ID: ");
            long studentId = Long.parseLong(scanner.nextLine().trim());

            List<EnrollmentRequestDto> list = enrollmentService.getEnrollmentsByStudentId(studentId);
            printEnrollmentTable(list);

        } catch (NumberFormatException e) {
            System.out.println(red + "Invalid input! Please enter a number.");
        } catch (EnrollmentException e) {
            System.out.println(red + "Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }

    // ── 5. UPDATE ─────────────────────────────────────────────────────────────
    public void updateEnrollment() {
        while (true) {
            try {
                System.out.print(yellow + "Enter Enrollment ID to update: ");
                int enrollmentId = Integer.parseInt(scanner.nextLine().trim());

                // Fetch existing record
                EnrollmentRequestDto current = enrollmentService.getEnrollmentById(enrollmentId);
                if (current == null) {
                    System.out.println(red + "No enrollment found with ID: " + enrollmentId);
                    return;
                }
                try {
//                    System.out.print("  Student ID  [" + current.getStudent_id() + "]: ");
//                    String studentInput = scanner.nextLine().trim();
//                    long studentId = studentInput.isEmpty()
//                            ? current.getStudent_id()
//                            : Long.parseLong(studentInput);


                    Long studentId = current.getStudent_id();
                    System.out.print("Course ID   [" + current.getCourse_id() + "]: ");
                    String courseInput = scanner.nextLine().trim();
                    int courseId = courseInput.isEmpty()
                            ? current.getCourse_id()
                            : Integer.parseInt(courseInput);

//                    System.out.print("Shift       [" + current.getShift() + "]: ");
//                    String shiftInput = scanner.nextLine().trim();
                    String shift = current.getShift();

                    EnrollmentRequestDto request = EnrollmentRequestDto.builder()
                            .enrollment_id(enrollmentId)
                            .student_id(studentId)
                            .course_id(courseId)
                            .shift(shift)
                            .build();

                    EnrollmentRequestDto updated = enrollmentService.updateEnrollment(request);
                    System.out.println(green + "\nEnrollment updated successfully!");
                    printEnrollmentTable(List.of(updated));

                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format: " + e.getMessage());
                } catch (EnrollmentException e) {
                    System.err.println(e.getMessage());
                } catch (Exception e) {
                    System.err.println("updateEnrollment() failed: " + e.getMessage());
                    e.printStackTrace();
                }
                return;

            } catch (NumberFormatException e) {
                System.out.println(red + "  Invalid input! Please enter a number.");
            }
        }
    }

    // ── 6. DELETE ─────────────────────────────────────────────────────────────
    public void deleteEnrollment() {
        try {
            System.out.print(yellow + "Enter Enrollment ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            enrollmentService.deleteEnrollment(id);
            System.out.println(green + "Enrollment with ID " + id + " deleted successfully.");

        } catch (NumberFormatException e) {
            System.out.println(red + "Invalid input! Please enter a number.");
        } catch (EnrollmentException e) {
            System.out.println(red + "Error [" + e.getStatusCode() + "]: " + e.getMessage());
        }
    }
}