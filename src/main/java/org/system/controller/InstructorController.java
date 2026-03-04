package org.system.controller;

import org.system.model.dto.response.InstructorResponseDto;
import org.system.service.InstructorService;

import java.sql.SQLException;
import java.util.Scanner;

public class InstructorController {

    private final Scanner scanner = new Scanner(System.in);
    private final InstructorService instructorService = new InstructorService();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    public void createInstructor() {
        System.out.println(cyan+"\n--- CREATE INSTRUCTOR ---");
        boolean validInput;

        // ── Instructor Name ──────────────────────────────────────
        String name = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Instructor Name : ");
            name = scanner.nextLine().trim();
            if (!name.isBlank() && name.matches("^[a-zA-Z\\s]+$")) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Name must be letters only and not empty.");
            }
        }

        // ── Gender ───────────────────────────────────────────────
        String gender = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Gender (Male/Female) : ");
            gender = scanner.nextLine().trim();
            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Please enter Male or Female.");
            }
        }

        // ── Age ──────────────────────────────────────────────────
        int age = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Age              : ");
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age > 0 && age < 120) {
                    validInput = true;
                } else {
                    System.out.println(red+"Invalid! Please enter a realistic age.");
                }
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid! Please enter a valid number.");
            }
        }

        // ── Email ────────────────────────────────────────────────
        String email = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Email            : ");
            email = scanner.nextLine().trim();
            if (!email.isBlank() && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Please enter a valid email (e.g. example@mail.com).");
            }
        }

        // ── Phone Number ─────────────────────────────────────────
        String phone = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Phone Number     : ");
            phone = scanner.nextLine().trim();
            if (!phone.isBlank() && phone.matches("^[0-9+\\-\\s]{7,15}$")) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Please enter a valid phone number.");
            }
        }

        // ── Address ──────────────────────────────────────────────
        String address = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Address          : ");
            address = scanner.nextLine().trim();
            if (!address.isBlank()) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Address cannot be empty.");
            }
        }

        // ── Qualification ────────────────────────────────────────
        String qualification = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Qualification    : ");
            qualification = scanner.nextLine().trim();
            if (!qualification.isBlank()) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Qualification cannot be empty.");
            }
        }

        // ── Build & Send ─────────────────────────────────────────
        InstructorResponseDto instructor = new InstructorResponseDto(
                0, name, gender, age, email, phone, address, qualification
        );

        boolean success = instructorService.createInstructor(instructor);
        System.out.println(success
                ? green+"[CONTROLLER] Instructor created successfully! ID = " + instructor.getInstructorId()
                : green +"[CONTROLLER] Failed to create instructor.");
    }

    public void displayAllInstructors() {
        instructorService.getAllInstructors();
    }

    public void displayInstructorById() {
        System.out.print(yellow+"Enter instructor_id : ");
        int id = readInt();
        instructorService.getInstructorById(id);
    }
    public void updateInstructor() {
        try{
            System.out.print(yellow+"Enter Instructor Id to update: ");
            int id = scanner.nextInt();
            instructorService.updateInstructor(id);
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input! Please input number");
        }
    }
//    public void updateInstructor() {
//        System.out.println(cyan + "\n--- UPDATE INSTRUCTOR ---");
//        boolean validInput;
//
//        // ── Instructor ID ────────────────────────────────────────
//        int id = 0;
//        validInput = false;
//        while (!validInput) {
//            try {
//                System.out.print(yellow + "Enter instructor_id to update : ");
//                id = Integer.parseInt(scanner.nextLine().trim());
//                validInput = true;
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid! Please enter a valid number.");
//            }
//        }
//
//        System.out.println(yellow + "Current data:");
//        instructorService.getInstructorById(id);
//        System.out.println(cyan + "(Press Enter to keep current value)\n");
//
//        // ── Name ─────────────────────────────────────────────────
//        String name = "";
//        validInput = false;
//        while (!validInput) {
//            System.out.print(yellow + "New Name          : ");
//            name = scanner.nextLine().trim();
//            if (name.isEmpty() || name.matches("^[a-zA-Z\\s]+$")) {
//                validInput = true;
//            } else {
//                System.out.println("Invalid! Name must be letters only.");
//            }
//        }
//
//        // ── Gender ───────────────────────────────────────────────
//        String gender = "";
//        validInput = false;
//        while (!validInput) {
//            System.out.print(yellow + "New Gender (Male/Female) : ");
//            gender = scanner.nextLine().trim();
//            if (gender.isEmpty() || gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
//                validInput = true;
//            } else {
//                System.out.println("Invalid! Please enter Male or Female.");
//            }
//        }
//
//        // ── Age ──────────────────────────────────────────────────
//        int age = 0;
//        validInput = false;
//        while (!validInput) {
//            try {
//                System.out.print(yellow + "New Age           : ");
//                String ageInput = scanner.nextLine().trim();
//                if (ageInput.isEmpty()) {
//                    validInput = true;
//                } else {
//                    age = Integer.parseInt(ageInput);
//                    if (age > 0 && age < 120) {
//                        validInput = true;
//                    } else {
//                        System.out.println("Invalid! Please enter a realistic age.");
//                    }
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid! Please enter a valid number.");
//            }
//        }
//
//        // ── Email ────────────────────────────────────────────────
//        String email = "";
//        validInput = false;
//        while (!validInput) {
//            System.out.print(yellow + "New Email         : ");
//            email = scanner.nextLine().trim();
//            if (email.isEmpty() || email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
//                validInput = true;
//            } else {
//                System.out.println("Invalid! Please enter a valid email (e.g. example@mail.com).");
//            }
//        }
//
//        // ── Phone ────────────────────────────────────────────────
//        String phone = "";
//        validInput = false;
//        while (!validInput) {
//            System.out.print(yellow + "New Phone Number  : ");
//            phone = scanner.nextLine().trim();
//            if (phone.isEmpty() || phone.matches("^[0-9+\\-\\s]{7,15}$")) {
//                validInput = true;
//            } else {
//                System.out.println("Invalid! Please enter a valid phone number.");
//            }
//        }
//
//        // ── Address ──────────────────────────────────────────────
//        System.out.print(yellow + "New Address       : ");
//        String address = scanner.nextLine().trim();
//
//        // ── Qualification ────────────────────────────────────────
//        System.out.print(yellow + "New Qualification : ");
//        String qualification = scanner.nextLine().trim();
//
//        // ── Build & Send ─────────────────────────────────────────
//        InstructorResponseDto updated = new InstructorResponseDto(
//                id,
//                name.isEmpty()          ? null : name,
//                gender.isEmpty()        ? null : gender,
//                age == 0                ? 0    : age,
//                email.isEmpty()         ? null : email,
//                phone.isEmpty()         ? null : phone,
//                address.isEmpty()       ? null : address,
//                qualification.isEmpty() ? null : qualification
//        );
//
//        try {
//            InstructorResponseDto result = instructorService.updateInstructor(id, updated);
//            if (result != null) {
//                System.out.println(green + "[CONTROLLER] Instructor updated successfully.");
//            } else {
//                System.out.println(red + "[CONTROLLER] Failed to update instructor.");
//            }
//        } catch (SQLException e) {
//            System.out.println(red + "[CONTROLLER] Error: " + e.getMessage());
//        }
//    }

    public void deleteInstructor() {
        System.out.println(cyan+"\n--- DELETE INSTRUCTOR ---");
        System.out.print(yellow+"Enter instructor_id to delete : ");
        int id = readInt();

        System.out.println(yellow+"Instructor to delete:");
        instructorService.getInstructorById(id);

        System.out.print(cyan+"Are you sure? (y/n) : ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            boolean success = instructorService.deleteInstructor(id);
            System.out.println(success
                    ? green +"[CONTROLLER] Instructor deleted successfully."
                    : green +"[CONTROLLER] Failed to delete instructor.");
        } else {
            System.out.println(red+"[CONTROLLER] Delete cancelled.");
        }
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(red+"[ERROR] Please enter a valid integer: ");
            }
        }
    }
}