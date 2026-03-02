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

    public void createSubject() {
        System.out.println("\n--- CREATE SUBJECT ---");
        boolean validInput;

        // ── Subject Name ─────────────────────────────────────────
        String name = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Subject Name : ");
            name = scanner.nextLine().trim();
            if (!name.isBlank() && name.matches("^[a-zA-Z\\s]+$")) {
                validInput = true;
            } else {
                System.out.println("Invalid! Name must be letters only and not empty.");
            }
        }

        // ── Description ──────────────────────────────────────────
        String desc = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Description  : ");
            desc = scanner.nextLine().trim();
            if (!desc.isBlank()) {
                validInput = true;
            } else {
                System.out.println("Invalid! Description cannot be empty.");
            }
        }

        // ── Hours ────────────────────────────────────────────────
        double hour = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Hours        : ");
                hour = Double.parseDouble(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        // ── Course ID ────────────────────────────────────────────
        int courseId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Course ID    : ");
                courseId = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        SubjectResponseDto subject = new SubjectResponseDto(0, name, desc, hour, courseId);
        boolean success = subjectService.createSubject(subject);
        if (success) {
            System.out.println("[CONTROLLER] Subject created successfully! sub_id = " + subject.getSub_id());
        } else {
            System.out.println("[CONTROLLER] Failed to create subject.");
        }
    }

    public void displayAllSubjects() {
        subjectService.getAllSubjects();
    }

    public void displaySubjectById() {
        boolean validInput;
        while (true) {
            System.out.println("""
                Do you want to Display One Subject:
                1. Display By Subject Id
                2. Display By Course Id
                0. Back
                """);

            int option = -1;
            validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Enter option: ");
                    option = Integer.parseInt(scanner.nextLine().trim());
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid! Please enter a valid number.");
                }
            }

            switch (option) {
                case 1 -> displaySubjectBySubjectId();
                case 2 -> displaySubjectsByCourse();
                case 0 -> { return; }
                default -> System.out.println("Please enter option (0-2)");
            }
        }
    }

    public void displaySubjectBySubjectId() {
        boolean validInput = false;
        int id = 0;
        while (!validInput) {
            try {
                System.out.print("Enter sub_id : ");
                id = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }
        subjectService.getSubjectById(id);
    }

    public void displaySubjectsByCourse() {
        boolean validInput = false;
        int courseId = 0;
        while (!validInput) {
            try {
                System.out.print("Enter course_id : ");
                courseId = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }
        subjectService.getSubjectsByCourseId(courseId);
    }

    public void updateSubject() {
        System.out.println("\n--- UPDATE SUBJECT ---");
        boolean validInput;

        // ── Sub ID ───────────────────────────────────────────────
        int id = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter sub_id to update : ");
                id = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        System.out.println("Current data:");
        subjectService.getSubjectById(id);
        System.out.println("(Press Enter to keep current value)\n");

        // ── Subject Name ─────────────────────────────────────────
        String name = "";
        validInput = false;
        while (!validInput) {
            System.out.print("New Subject Name : ");
            name = scanner.nextLine().trim();
            if (name.isEmpty() || name.matches("^[a-zA-Z\\s]+$")) {
                validInput = true;
            } else {
                System.out.println("Invalid! Name must be letters only.");
            }
        }

        // ── Description ──────────────────────────────────────────
        System.out.print("New Description  : ");
        String desc = scanner.nextLine().trim();

        // ── Hours ────────────────────────────────────────────────
        double hour = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("New Hours        : ");
                String hourInput = scanner.nextLine().trim();
                hour = hourInput.isEmpty() ? 0 : Double.parseDouble(hourInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        // ── Course ID ────────────────────────────────────────────
        int courseId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("New Course ID    : ");
                String courseInput = scanner.nextLine().trim();
                courseId = courseInput.isEmpty() ? 0 : Integer.parseInt(courseInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        SubjectResponseDto updated = new SubjectResponseDto(
                id,
                name.isEmpty()  ? null : name,
                desc.isEmpty()  ? null : desc,
                hour,
                courseId
        );

        boolean success = subjectService.updateSubject(updated);
        if (success) {
            System.out.println("[CONTROLLER] Subject updated successfully.");
        } else {
            System.out.println("[CONTROLLER] Failed to update subject.");
        }
    }

    public void deleteSubject() {
        System.out.println("\n--- DELETE SUBJECT ---");
        boolean validInput;

        // ── Sub ID ───────────────────────────────────────────────
        int id = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter sub_id to delete : ");
                id = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        System.out.println("Subject to delete:");
        subjectService.getSubjectById(id);

        // ── Confirm ──────────────────────────────────────────────
        validInput = false;
        while (!validInput) {
            System.out.print("Are you sure? (yes/no) : ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes") || confirm.equals("no")) {
                validInput = true;
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
            } else {
                System.out.println("Invalid! Please enter 'yes' or 'no'.");
            }
        }
    }
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
