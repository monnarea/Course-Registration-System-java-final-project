package org.system.controller;

import org.system.model.dto.response.SubjectResponseDto;
import org.system.service.SubjectService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static jdk.internal.jimage.decompressor.CompressIndexes.readInt;

public class SubjectController {
    private final Scanner scanner = new Scanner(System.in);
    private final SubjectService subjectService = new SubjectService();
    // =========================================================
    //  CREATE
    // =========================================================
    public void createSubject() {
        System.out.println("\n--- CREATE SUBJECT ---");

        System.out.print("Subject Name : ");
        String name = scanner.nextLine().trim();

        System.out.print("Description  : ");
        String desc = scanner.nextLine().trim();

        System.out.print("Hours        : ");
        double hour = readDouble();

        System.out.print("Course ID    : ");
        int courseId = readInt();

        SubjectResponseDto subject = new SubjectResponseDto(0, name, desc, hour, courseId);

        boolean success = subjectService.createSubject(subject);
        if (success) {
            System.out.println("[CONTROLLER] Subject created successfully! sub_id = " + subject.getSub_id());
        } else {
            System.out.println("[CONTROLLER] Failed to create subject.");
        }
    }



    // =========================================================
    //  READ ALL
    // =========================================================
    public void displayAllSubjects() {
        subjectService.getAllSubjects();
    }

    // =========================================================
    //  READ BY ID
    // =========================================================
    public void displaySubjectById() {
        while (true) {
            System.out.println("""
                    Do you want to Display One Subject:
                    1. Display By Subject Id
                    2. Display By Course Id
                    0. Back
                    """);
            System.out.print("Enter option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> displaySubjectBySubjectId();
                case 2 -> displaySubjectsByCourse();
                case 0 -> { return; }
                default -> {
                    System.out.println("Please enter option (1-2)");
                }
            }
        }
    }

    public void displaySubjectBySubjectId() {
        System.out.print("Enter sub_id : ");
        int id = readInt();
        subjectService.getSubjectById(id);
    }


    // =========================================================
    //  READ BY COURSE ID
    // =========================================================
    public void displaySubjectsByCourse() {
        System.out.print("Enter course_id : ");
        int courseId = readInt();
        subjectService.getSubjectsByCourseId(courseId);
    }

    // =========================================================
    //  UPDATE
    // =========================================================
    public void updateSubject() {
        System.out.println("\n--- UPDATE SUBJECT ---");
        System.out.print("Enter sub_id to update : ");
        int id = readInt();
        // Show current data first
        System.out.println("Current data:");
        subjectService.getSubjectById(id);

        System.out.println("(Press Enter to keep current value)\n");

        System.out.print("New Subject Name : ");
        String name = scanner.nextLine().trim();

        System.out.print("New Description  : ");
        String desc = scanner.nextLine().trim();

        System.out.print("New Hours        : ");
        String hourInput = scanner.nextLine().trim();

        System.out.print("New Course ID    : ");
        String courseInput = scanner.nextLine().trim();

        // Build updated DTO only with provided values
        // (fetch existing first to keep unchanged fields)
        SubjectResponseDto updated = new SubjectResponseDto(
                id,
                name.isEmpty()       ? null                        : name,
                desc.isEmpty()       ? null                        : desc,
                hourInput.isEmpty()  ? 0                           : Double.parseDouble(hourInput),
                courseInput.isEmpty()? 0                           : Integer.parseInt(courseInput)
        );

        boolean success = subjectService.updateSubject(updated);
        if (success) {
            System.out.println("[CONTROLLER] Subject updated successfully.");
        } else {
            System.out.println("[CONTROLLER] Failed to update subject.");
        }
    }

    // =========================================================
    //  DELETE
    // =========================================================
    public void deleteSubject() {
        System.out.println("\n--- DELETE SUBJECT ---");
        System.out.print("Enter sub_id to delete : ");
        int id = scanner.nextInt();

        // Show subject before confirming
        System.out.println("Subject to delete:");
        subjectService.getSubjectById(id);

        System.out.print("Are you sure? (yes/no) : ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            boolean success = subjectService.deleteSubject(id);
            if (success) {
                System.out.println("[CONTROLLER] Subject deleted successfully.");
            } else {
                System.out.println("[CONTROLLER] Failed to delete subject.");
            }
        } else {
            System.out.println("[CONTROLLER] Delete cancelled.");
        }
    }

    // =========================================================
    //  HELPERS — Safe input reading
    // =========================================================
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Please enter a valid integer: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("[ERROR] Please enter a valid number: ");
            }
        }
    }
}
