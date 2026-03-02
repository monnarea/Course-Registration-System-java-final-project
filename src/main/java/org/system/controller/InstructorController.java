package org.system.controller;

import org.system.model.dto.response.InstructorResponseDto;
import org.system.service.InstructorService;

import java.util.Scanner;

public class InstructorController {

    private final Scanner scanner = new Scanner(System.in);
    private final InstructorService instructorService = new InstructorService();

    public void createInstructor() {
        System.out.println("\n--- CREATE INSTRUCTOR ---");
        boolean validInput;

        // ── Instructor Name ──────────────────────────────────────
        String name = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Instructor Name : ");
            name = scanner.nextLine().trim();
            if (!name.isBlank() && name.matches("^[a-zA-Z\\s]+$")) {
                validInput = true;
            } else {
                System.out.println("Invalid! Name must be letters only and not empty.");
            }
        }

        // ── Gender ───────────────────────────────────────────────
        String gender = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Gender (Male/Female) : ");
            gender = scanner.nextLine().trim();
            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
                validInput = true;
            } else {
                System.out.println("Invalid! Please enter Male or Female.");
            }
        }

        // ── Age ──────────────────────────────────────────────────
        int age = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Age              : ");
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age > 0 && age < 120) {
                    validInput = true;
                } else {
                    System.out.println("Invalid! Please enter a realistic age.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        // ── Email ────────────────────────────────────────────────
        String email = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Email            : ");
            email = scanner.nextLine().trim();
            if (!email.isBlank() && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                validInput = true;
            } else {
                System.out.println("Invalid! Please enter a valid email (e.g. example@mail.com).");
            }
        }

        // ── Phone Number ─────────────────────────────────────────
        String phone = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Phone Number     : ");
            phone = scanner.nextLine().trim();
            if (!phone.isBlank() && phone.matches("^[0-9+\\-\\s]{7,15}$")) {
                validInput = true;
            } else {
                System.out.println("Invalid! Please enter a valid phone number.");
            }
        }

        // ── Address ──────────────────────────────────────────────
        String address = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Address          : ");
            address = scanner.nextLine().trim();
            if (!address.isBlank()) {
                validInput = true;
            } else {
                System.out.println("Invalid! Address cannot be empty.");
            }
        }

        // ── Qualification ────────────────────────────────────────
        String qualification = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Qualification    : ");
            qualification = scanner.nextLine().trim();
            if (!qualification.isBlank()) {
                validInput = true;
            } else {
                System.out.println("Invalid! Qualification cannot be empty.");
            }
        }

        // ── Build & Send ─────────────────────────────────────────
        InstructorResponseDto instructor = new InstructorResponseDto(
                0, name, gender, age, email, phone, address, qualification
        );

        boolean success = instructorService.createInstructor(instructor);
        System.out.println(success
                ? "[CONTROLLER] Instructor created successfully! ID = " + instructor.getInstructorId()
                : "[CONTROLLER] Failed to create instructor.");
    }

    public void displayAllInstructors() {
        instructorService.getAllInstructors();
    }

    public void displayInstructorById() {
        System.out.print("Enter instructor_id : ");
        int id = readInt();
        instructorService.getInstructorById(id);
    }

    public void updateInstructor() {
        System.out.println("\n--- UPDATE INSTRUCTOR ---");
        System.out.print("Enter instructor_id to update : ");
        int id = readInt();

        System.out.println("Current data:");
        instructorService.getInstructorById(id);
        System.out.println("(Press Enter to keep current value)\n");

        System.out.print("New Name          : "); String name  = scanner.nextLine().trim();
        System.out.print("New Gender        : "); String gender = scanner.nextLine().trim();
        System.out.print("New Age           : "); String ageInput = scanner.nextLine().trim();
        System.out.print("New Email         : "); String email = scanner.nextLine().trim();
        System.out.print("New Phone Number  : "); String phone = scanner.nextLine().trim();
        System.out.print("New Address       : "); String address = scanner.nextLine().trim();
        System.out.print("New Qualification : "); String qualification = scanner.nextLine().trim();

        InstructorResponseDto updated = new InstructorResponseDto(
                id,
                name.isEmpty()         ? null : name,
                gender.isEmpty()       ? null : gender,
                ageInput.isEmpty()     ? 0    : Integer.parseInt(ageInput),
                email.isEmpty()        ? null : email,
                phone.isEmpty()        ? null : phone,
                address.isEmpty()      ? null : address,
                qualification.isEmpty()? null : qualification
        );

        boolean success = instructorService.updateInstructor(updated);
        System.out.println(success
                ? "[CONTROLLER] Instructor updated successfully."
                : "[CONTROLLER] Failed to update instructor.");
    }

    public void deleteInstructor() {
        System.out.println("\n--- DELETE INSTRUCTOR ---");
        System.out.print("Enter instructor_id to delete : ");
        int id = readInt();

        System.out.println("Instructor to delete:");
        instructorService.getInstructorById(id);

        System.out.print("Are you sure? (yes/no) : ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            boolean success = instructorService.deleteInstructor(id);
            System.out.println(success
                    ? "[CONTROLLER] Instructor deleted successfully."
                    : "[CONTROLLER] Failed to delete instructor.");
        } else {
            System.out.println("[CONTROLLER] Delete cancelled.");
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
}