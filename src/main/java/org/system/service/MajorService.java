package org.system.service;

import org.system.model.dao.MajorDaoImpl;
import org.system.model.dto.request.CourseRequestDto;
import org.system.model.dto.request.MajorRequestDto;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.MajorResponseDto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printCourseTable;
import static org.system.view.View.printMajorTable;

public class MajorService {
    private final Scanner scanner = new Scanner(System.in);
    private final MajorDaoImpl majorDao = new MajorDaoImpl();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";
    public void displayAllMajor(){
        try {
            List<MajorResponseDto> allMajor = majorDao.getAll();

            if (allMajor.isEmpty()) {
                System.out.println(red+"No Major found in database.");
            } else {
                System.out.println(cyan+"Total Major found: " + allMajor.size());

                printMajorTable(allMajor);

            }
        } catch (SQLException e) {
            System.err.println(red+"getAll() failed: " + e.getMessage());
        }

        System.out.println();

    }


    public void displayMajorById(int majorId){
        try {
            List<MajorResponseDto> allMajor = majorDao.getById(majorId);

            if (allMajor.isEmpty()) {

                System.out.println("No Major found in database.");
            } else {
                System.out.println("Total Major found: " + allMajor.size());

                printMajorTable(allMajor);

            }
        } catch (SQLException e) {
            System.err.println("getMajorById() failed: " + e.getMessage());
        }

        System.out.println();
    }

    public void create(){

        System.out.println(cyan+"\n========== Create New Major ==========");

        boolean validInput;

        // Course Name
//        validInput = false;
//        while (!validInput) {
//            System.out.print("Major Name       : ");
//            String MajorName = scanner.nextLine();
//            if (!MajorName.isBlank() && MajorName.matches("^[a-zA-Z\\s]+$")) {
//                validInput = true;
//            } else {
//                System.out.println("Invalid! Name must be letters only and not empty.");
//            }
//        }
//        validInput = false;
//        System.out.print("Major description: ");
//        String Description = scanner.nextLine();
//        MajorRequestDto request = new MajorRequestDto(
//                MajorName,Description
//        );
        try {
            validInput = false;
            String MajorName = null;
            while (!validInput) {
                System.out.print(yellow+"Major Name       : ");
                MajorName = scanner.nextLine();
                if (!MajorName.isBlank() && MajorName.matches("^[a-zA-Z\\s]+$")) {
                    validInput = true;
                } else {
                    System.out.println(red+"Invalid! Name must be letters only and not empty.");
                }
            }

            System.out.print(yellow+"Major description: ");
            String Description = scanner.nextLine();


            MajorRequestDto request = new MajorRequestDto(
                    MajorName, Description
            );

            MajorResponseDto created = majorDao.create(request);
            System.out.println(green+"\n✔ Major created successfully!");
            printMajorTable(List.of(created));

        } catch (SQLException e) {
            System.err.println(red+"create() failed: " + e.getMessage());
        }
        System.out.println();
    }

    public void update(int majorId){
        System.out.println("\n========== Update Major (ID: " + majorId+ ") ==========");

        // Step 1: Fetch existing course
        MajorResponseDto current;
        try {
            List<MajorResponseDto> existing = majorDao.getById(majorId);
            if (existing.isEmpty()) {
                System.out.println("No Major found with ID: " + majorId);
                return;
            }
            current = existing.get(0);
            System.out.println("Current details:");
            printMajorTable(existing);
        } catch (SQLException e) {
            System.err.println("Could not fetch Major: " + e.getMessage());
            return;
        }

        System.out.println("\nPress Enter to keep the current value, or type a new one:");

        try {
            System.out.print("Major Name      [" + current.getMajor_name() + "]: ");
            String majorNameInput = scanner.nextLine().trim();
            String majorName = majorNameInput.isEmpty() ? current.getMajor_name() : majorNameInput;

            System.out.print("Description            [" + current.getDescription() + "]: ");
            String descriptionInput = scanner.nextLine().trim();
            String description = descriptionInput.isEmpty() ? current.getDescription() : descriptionInput;

            MajorRequestDto request = new MajorRequestDto(
                    majorName,description
            );

            MajorResponseDto updated = majorDao.update(majorId,request);
            System.out.println("\n✔ Major updated successfully!");
            printMajorTable(List.of(updated));

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format entered: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("update() failed: " + e.getMessage());
        }

        System.out.println();
    }

    public void delete(int majorId){
        System.out.println(cyan+"\n========== Delete Course (ID: " + majorId + ") ==========");

        // Show the course before deleting so the user knows what will be removed
        try {
            List<MajorResponseDto> existing = majorDao.getById(majorId);
            if (existing.isEmpty()) {
                System.out.println(red+"No Major found with ID: " + majorId);
                return;
            }
            printMajorTable(existing);
        } catch (SQLException e) {
            System.err.println(red+"Could not fetch major: " + e.getMessage());
            return;
        }

        System.out.print(cyan+"Are you sure you want to delete this major? (y/n): ");
        String confirm = scanner.next().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println(red+"Delete cancelled.");
            return;
        }

        try {
            boolean deleted = majorDao.delete(majorId);
            if (deleted) {
                System.out.println(green+"✔ Major with ID " + majorId + " deleted successfully.");
            } else {
                System.out.println(red+"✘ Delete failed — no major found with ID: " + majorId);
            }
        } catch (SQLException e) {
            System.err.println(red+"delete() failed: " + e.getMessage());
        }
        System.out.println();
    }
}
