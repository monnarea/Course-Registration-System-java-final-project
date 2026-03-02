package org.system.service;

import org.system.model.dao.CourseDaoImpl;
import org.system.model.dto.request.CourseRequestDto;
import org.system.model.dto.response.CourseResponseDto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printCourseTable;
import static org.system.view.View.printSingleCourseTable;

public class CourseService {

    private final CourseDaoImpl courseDao = new CourseDaoImpl();
    private final CourseDaoImpl singleCourseDao = new CourseDaoImpl();
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void displayAllCourse() {

        try {
            List<CourseResponseDto> allCourses = courseDao.getAll();

            if (allCourses.isEmpty()) {
                System.out.println("No courses found in database.");
            } else {
                System.out.println("Total courses found: " + allCourses.size());

                printCourseTable(allCourses);

            }
        } catch (SQLException e) {
            System.err.println("getAll() failed: " + e.getMessage());
        }

        System.out.println();

    }

    public void displaySingleCourseByCourseId(int course_id) {

        try {
            List<CourseResponseDto> Courses = courseDao.getById(course_id);

            if (Courses.isEmpty()) {
                System.out.println("No courses found in database.");
            } else {
                System.out.println("Total courses found: " + Courses.size());

                printCourseTable(Courses);

            }
        } catch (SQLException e) {
            System.err.println("getAll() failed: " + e.getMessage());
        }

        System.out.println();

    }

    public void displaySingleCourseByMajorId(int major_id) {
        try {
            List<CourseResponseDto> result = singleCourseDao.getByMajorId(major_id);// change 1 to any ID
            if (result.isEmpty()) {
                System.out.println("No course found.");
            } else {
                printSingleCourseTable(result); // reuse same printTable method
            }
        } catch (SQLException e) {
            System.err.println("getByMajorId() failed: " + e.getMessage());
        }


    }

    public void createCourse() throws SQLException {
        System.out.println("\n========== Create New Course ==========");

        // Declare all variables up front
        String courseName = "";
        double price = 0;
        int creditScore = 0, capacity = 0, instructorId = 0, majorId = 0, level = 0;
        String room = "";
        LocalDate startDate = null, endDate = null;
        boolean validInput;

        // Course Name
        validInput = false;
        while (!validInput) {
            System.out.print("Course Name      : ");
            courseName = scanner.nextLine();
            if (!courseName.isBlank() && courseName.matches("^[a-zA-Z\\s]+$")) {
                validInput = true;
            } else {
                System.out.println("Invalid! Name must be letters only and not empty.");
            }
        }

        // Price
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Price            : ");
                price = scanner.nextDouble();
                scanner.nextLine();
                validInput = true; // ✅ success
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // ✅ clear buffer
            }
        }

        // Credit Score
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Credit Score     : ");
                creditScore = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Capacity
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Capacity         : ");
                capacity = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Instructor ID
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Instructor ID    : ");
                instructorId = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Room
        validInput = false;
        while (!validInput) {
            System.out.print("Room             : ");
            room = scanner.nextLine();
            if (!room.isBlank()) {
                validInput = true;
            } else {
                System.out.println("Invalid! Room cannot be empty.");
            }
        }

        // Major ID
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Major ID         : ");
                majorId = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Level
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Level            : ");
                level = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Start Date
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Start Date (yyyy-MM-dd): ");
                startDate = LocalDate.parse(scanner.nextLine().trim());
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // End Date
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("End Date (yyyy-MM-dd): ");
                endDate = LocalDate.parse(scanner.nextLine().trim());
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // Create course
        CourseRequestDto request = new CourseRequestDto(
                courseName, price, creditScore, capacity,
                startDate, endDate, instructorId, room, majorId, level
        );
        CourseResponseDto created = courseDao.create(request);
        System.out.println("\n✔ Course created successfully!");
        printCourseTable(List.of(created));
    }


    public void updateCourse(int course_id) {
        boolean validInput;
        System.out.println("\n========== Update Course (ID: " + course_id + ") ==========");

        CourseResponseDto current;
        try {
            List<CourseResponseDto> existing = courseDao.getById(course_id);
            if (existing.isEmpty()) {
                System.out.println("No course found with ID: " + course_id);
                return;
            }
            current = existing.get(0);
            System.out.println("Current details:");
            printCourseTable(existing);
        } catch (SQLException e) {
            System.err.println("Could not fetch course: " + e.getMessage());
            return;
        }

        System.out.println("\nPress Enter to keep the current value, or type a new one:");

        String courseName = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Course Name      [" + current.getCourse_name() + "]: ");
                String courseNameInput = scanner.nextLine().trim();
                courseName = courseNameInput.isEmpty() ? current.getCourse_name() : courseNameInput;
                if (!courseName.isBlank() && courseName.matches("^[a-zA-Z\\s]+$")) {
                    validInput = true;
                } else {
                    System.out.println("Invalid! Name must be letters only and not empty.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }

        double price = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Price            [" + current.getPrice() + "]: ");
                String priceInput = scanner.nextLine().trim();
                price = priceInput.isEmpty() ? current.getPrice() : Double.parseDouble(priceInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        int creditScore = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Credit Score     [" + current.getCredit_score() + "]: ");
                String creditInput = scanner.nextLine().trim();
                creditScore = creditInput.isEmpty() ? current.getCredit_score() : Integer.parseInt(creditInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        int capacity = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Capacity         [" + current.getCapacity() + "]: ");
                String capacityInput = scanner.nextLine().trim();
                capacity = capacityInput.isEmpty() ? current.getCapacity() : Integer.parseInt(capacityInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        LocalDate startDate = null;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Start Date       [" + current.getStart_date() + "] (yyyy-MM-dd): ");
                String startInput = scanner.nextLine().trim();
                startDate = startInput.isEmpty() ? current.getStart_date() : LocalDate.parse(startInput);
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        LocalDate endDate = null;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("End Date         [" + current.getEnd_date() + "] (yyyy-MM-dd): ");
                String endInput = scanner.nextLine().trim();
                endDate = endInput.isEmpty() ? current.getEnd_date() : LocalDate.parse(endInput);
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        int instructorId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Instructor ID    [" + current.getInstructor_id() + "]: ");
                String instructorInput = scanner.nextLine().trim();
                instructorId = instructorInput.isEmpty() ? current.getInstructor_id() : Integer.parseInt(instructorInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        String room = "";
        validInput = false;
        while (!validInput) {
            System.out.print("Room             [" + current.getRoom() + "]: ");
            String roomInput = scanner.nextLine().trim();
            room = roomInput.isEmpty() ? current.getRoom() : roomInput;
            if (!room.isBlank()) {
                validInput = true;
            } else {
                System.out.println("Invalid! Room cannot be empty.");
            }
        }

        int majorId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Major ID         [" + current.getMajor_id() + "]: ");
                String majorInput = scanner.nextLine().trim();
                majorId = majorInput.isEmpty() ? current.getMajor_id() : Integer.parseInt(majorInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        int level = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Level            [" + current.getLevel() + "]: ");
                String levelInput = scanner.nextLine().trim();
                level = levelInput.isEmpty() ? current.getLevel() : Integer.parseInt(levelInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }

        try {
            CourseRequestDto request = new CourseRequestDto(
                    courseName, price, creditScore, capacity,
                    startDate, endDate, instructorId, room, majorId, level
            );
            CourseResponseDto updated = courseDao.update(course_id, request);
            System.out.println("\n✔ Course updated successfully!");
            printCourseTable(List.of(updated));
        } catch (SQLException e) {
            System.err.println("update() failed: " + e.getMessage());
        }
    }


    public void deleteCourse(int course_id) {
        System.out.println("\n========== Delete Course (ID: " + course_id + ") ==========");

        // Show the course before deleting so the user knows what will be removed
        try {
            List<CourseResponseDto> existing = courseDao.getById(course_id);
            if (existing.isEmpty()) {
                System.out.println("No course found with ID: " + course_id);
                return;
            }
            printCourseTable(existing);
        } catch (InputMismatchException i) {
            System.out.println("Invalid input! Please input number");
        } catch (SQLException e) {
            System.err.println("Could not fetch course: " + e.getMessage());
            return;
        }

        System.out.print("Are you sure you want to delete this course? (yes/no): ");
        String confirm = scanner.next().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println("Delete cancelled.");
            return;
        }

        try {
            boolean deleted = courseDao.delete(course_id);
            if (deleted) {
                System.out.println("✔ Course with ID " + course_id + " deleted successfully.");
            } else {
                System.out.println("✘ Delete failed — no course found with ID: " + course_id);
            }
        } catch (SQLException e) {
            System.err.println("delete() failed: " + e.getMessage());
        }
        System.out.println();
    }

    // ══════════════════════════════════════════════
    // HELPER
    // ══════════════════════════════════════════════

    private LocalDate parseDate(String input) {
        return LocalDate.parse(input, DATE_FORMATTER);
    }

    public void displayCourseById() {

        while (true){
        System.out.println("""
                ┌─────────────────────────────────┐
                │     Display Course By           │
                │  1. Major ID                    │
                │  2. Course ID                   │
                │  0. Back                        │
                └─────────────────────────────────┘""");
        try {
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.print("Enter Major ID: ");
                    displaySingleCourseByMajorId(scanner.nextInt());
                }
                case 2 -> {
                    System.out.print("Enter Course ID: ");
                    displaySingleCourseByCourseId(scanner.nextInt());
                }
                case 0 ->{return ;}
                default -> {
                    System.out.println("Invalid option. Please enter 1, 2 or 0.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input ! Please input number");
            scanner.nextLine();
        }
        }
    }
}

