package org.system.controller;

import org.system.model.dto.response.InstructorResponseDto;
import org.system.service.InstructorService;

import java.util.Scanner;

public class InstructorController {

    private final Scanner scanner = new Scanner(System.in);
    private final InstructorService instructorService = new InstructorService();

    // =========================================================
    //  CREATE
    // =========================================================
    public void createInstructor() {
        System.out.println("\n--- CREATE INSTRUCTOR ---");

        System.out.print("Instructor Name : ");
        String name = scanner.nextLine().trim();

        System.out.print("Gender          : ");
        String gender = scanner.nextLine().trim();

        System.out.print("Age             : ");
        int age = readInt();

        System.out.print("Email           : ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone Number    : ");
        String phone = scanner.nextLine().trim();

        System.out.print("Address         : ");
        String address = scanner.nextLine().trim();

        System.out.print("Qualification   : ");
        String qualification = scanner.nextLine().trim();

        InstructorResponseDto instructor = new InstructorResponseDto(
                0, name, gender, age, email, phone, address, qualification
        );

        boolean success = instructorService.createInstructor(instructor);
        System.out.println(success
                ? "[CONTROLLER] Instructor created successfully! ID = " + instructor.getInstructorId()
                : "[CONTROLLER] Failed to create instructor.");
    }

    // =========================================================
    //  READ ALL
    // =========================================================
    public void displayAllInstructors() {
        instructorService.getAllInstructors();
    }

    // =========================================================
    //  READ BY ID
    // =========================================================
    public void displayInstructorById() {
        System.out.print("Enter instructor_id : ");
        int id = readInt();
        instructorService.getInstructorById(id);
    }

    // =========================================================
    //  UPDATE
    // =========================================================
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

    // =========================================================
    //  DELETE
    // =========================================================
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

    // =========================================================
    //  HELPERS
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
}