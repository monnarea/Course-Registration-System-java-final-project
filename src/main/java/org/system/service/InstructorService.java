package org.system.service;

import org.system.model.dao.InstructorDao;
import org.system.model.dao.InstructorDaoImpl;
import org.system.model.dto.response.InstructorResponseDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printInstructorTable;

public class InstructorService {

    private final InstructorDao instructorDao = new InstructorDaoImpl();
    private final Scanner scanner = new Scanner(System.in);
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    // =========================================================
    //  CREATE
    // =========================================================
    public boolean createInstructor(InstructorResponseDto instructor) {
        if (instructor == null) {
            System.err.println("[SERVICE - CREATE] Instructor cannot be null.");
            return false;
        }
        if (instructor.getInstructorName() == null || instructor.getInstructorName().trim().isEmpty()) {
            System.err.println("[SERVICE - CREATE] Instructor name cannot be empty.");
            return false;
        }
        if (instructor.getEmail() == null || instructor.getEmail().trim().isEmpty()) {
            System.err.println("[SERVICE - CREATE] Email cannot be empty.");
            return false;
        }
        if (instructor.getAge() <= 0) {
            System.err.println("[SERVICE - CREATE] Age must be greater than 0.");
            return false;
        }
        return instructorDao.createInstructor(instructor);
    }

    // =========================================================
    //  READ ALL
    // =========================================================
    public void getAllInstructors() {
        List<InstructorResponseDto> list = instructorDao.getAllInstructors();
        if (list.isEmpty()) {
            System.err.println("[SERVICE - READ ALL] No instructors found.");
            return;
        }
        printInstructorTable(list);
    }

    // =========================================================
    //  READ BY ID
    // =========================================================
    public InstructorResponseDto getInstructorById(int instructorId) {
        if (instructorId <= 0) {
            System.err.println("[SERVICE - READ BY ID] Invalid instructor_id: " + instructorId);
            return null;
        }
        List<InstructorResponseDto> list = instructorDao.getInstructorById(instructorId);
        if (list.isEmpty()) {
            System.err.println("[SERVICE - READ BY ID] No instructor found with id = " + instructorId);
            return null;
        }
        printInstructorTable(list);
        return null;
    }

    // =========================================================
    //  UPDATE
    // =========================================================
    public void updateInstructor(int id) {
        boolean validInput;
        System.out.println(cyan + "\n========== Update Instructor (ID: " + id + ") ==========");

        // ── Fetch current data ───────────────────────────────────
        InstructorResponseDto current;
        List<InstructorResponseDto> existing = instructorDao.getInstructorById(id);
        if (existing.isEmpty()) {
            System.out.println(yellow + "No instructor found with ID: " + id);
            return;
        }
        current = existing.get(0);
        System.out.println(yellow + "Current details:");
        printInstructorTable(existing);

        System.out.println(cyan + "\nPress Enter to keep the current value, or type a new one:");

        // ── Name ─────────────────────────────────────────────────
        String name = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow + "Name          [" + current.getInstructorName() + "]: ");
            String nameInput = scanner.nextLine().trim();
            name = nameInput.isEmpty() ? current.getInstructorName() : nameInput;
            if (!name.isBlank() && name.matches("^[a-zA-Z\\s]+$")) {
                validInput = true;
            } else {
                System.out.println(red + "Invalid! Name must be letters only and not empty.");
            }
        }

        // ── Gender ───────────────────────────────────────────────
        String gender = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow + "Gender        [" + current.getGender() + "]: ");
            String genderInput = scanner.nextLine().trim();
            gender = genderInput.isEmpty() ? current.getGender() : genderInput;
            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
                validInput = true;
            } else {
                System.out.println(red + "Invalid! Please enter Male or Female.");
            }
        }

        // ── Age ──────────────────────────────────────────────────
        int age = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Age           [" + current.getAge() + "]: ");
                String ageInput = scanner.nextLine().trim();
                age = ageInput.isEmpty() ? current.getAge() : Integer.parseInt(ageInput);
                if (age > 0 && age < 120) {
                    validInput = true;
                } else {
                    System.out.println(red + "Invalid! Please enter a realistic age.");
                }
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a valid number.");
            }
        }

        // ── Email ────────────────────────────────────────────────
        String email = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow + "Email         [" + current.getEmail() + "]: ");
            String emailInput = scanner.nextLine().trim();
            email = emailInput.isEmpty() ? current.getEmail() : emailInput;
            if (!email.isBlank() && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                validInput = true;
            } else {
                System.out.println(red + "Invalid! Please enter a valid email (e.g. example@mail.com).");
            }
        }

        // ── Phone ────────────────────────────────────────────────
        String phone = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow + "Phone Number  [" + current.getPhoneNumber() + "]: ");
            String phoneInput = scanner.nextLine().trim();
            phone = phoneInput.isEmpty() ? current.getPhoneNumber() : phoneInput;
            if (!phone.isBlank() && phone.matches("^[0-9+\\-\\s]{7,15}$")) {
                validInput = true;
            } else {
                System.out.println(red + "Invalid! Please enter a valid phone number.");
            }
        }

        // ── Address ──────────────────────────────────────────────
        String address = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Address       [" + current.getAddress() + "]: ");
                String addressInput = scanner.nextLine().trim();
                address = addressInput.isEmpty() ? current.getAddress() : addressInput;
                if (!address.isBlank()) {
                    validInput = true;
                } else {
                    System.out.println(red + "Invalid! Address cannot be empty.");
                }
            } catch (Exception e) {
                System.out.println(red + "Invalid input!");
            }
        }

        // ── Qualification ────────────────────────────────────────
        String qualification = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Qualification [" + current.getQualification() + "]: ");
                String qualInput = scanner.nextLine().trim();
                qualification = qualInput.isEmpty() ? current.getQualification() : qualInput;
                if (!qualification.isBlank()) {
                    validInput = true;
                } else {
                    System.out.println(red + "Invalid! Qualification cannot be empty.");
                }
            } catch (Exception e) {
                System.out.println(red + "Invalid input!");
            }
        }

        // ── Build & Send ─────────────────────────────────────────
        try {
            InstructorResponseDto updated = new InstructorResponseDto(
                    id, name, gender, age, email, phone, address, qualification
            );
            InstructorResponseDto result = instructorDao.updateInstructor(id, updated);
            System.out.println(green + "\n✔ Instructor updated successfully!");
            printInstructorTable(List.of(result));
        } catch (SQLException e) {
            System.err.println(red + "Update failed: " + e.getMessage());
        }
    }

    // =========================================================
    //  DELETE
    // =========================================================
    public boolean deleteInstructor(int instructorId) {
        if (instructorId <= 0) {
            System.err.println("[SERVICE - DELETE] Invalid instructor_id: " + instructorId);
            return false;
        }
        List<InstructorResponseDto> existing = instructorDao.getInstructorById(instructorId);
        if (existing.isEmpty()) {
            System.err.println("[SERVICE - DELETE] Instructor not found with id: " + instructorId);
            return false;
        }
        return instructorDao.deleteInstructor(instructorId);
    }
}