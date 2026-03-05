package org.system.service;

import org.system.model.dao.CourseDaoImpl;
import org.system.model.dao.InstructorDaoImpl;
import org.system.model.dto.request.CourseRequestDto;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.InstructorResponseDto;

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
    private final InstructorDaoImpl instructorDao = new InstructorDaoImpl();
    private final Scanner scanner = new Scanner(System.in);
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";
    public static final String reset = "\u001B[0m";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void displayAllCourse() {

        try {
            List<CourseResponseDto> allCourses = courseDao.getAll();

            if (allCourses.isEmpty()) {

                System.out.println(red+"No courses found in database.");
            } else {
                System.out.println(cyan+"Total courses found: " + allCourses.size() + reset);

                printCourseTable(allCourses);

            }
        } catch (SQLException e) {
            System.err.println(red+"getAll() failed: " + e.getMessage());
        }

        System.out.println();

    }

    public void displaySingleCourseByCourseId(int course_id) {

        try {
            List<CourseResponseDto> Courses = courseDao.getById(course_id);

            if (Courses.isEmpty()) {
                System.out.println(red+"No courses found in database.");
            } else {
                System.out.println(cyan+"Total courses found: " + Courses.size());

                printCourseTable(Courses);

            }
        } catch (SQLException e) {
            System.err.println(red+"getAll() failed: " + e.getMessage());
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
        System.out.println(cyan+"\n========== Create New Course ==========");

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
            System.out.print(yellow+"Course Name      : ");
            courseName = scanner.nextLine();
            if (!courseName.isBlank() && courseName.matches("^[a-zA-Z\\s+#-]+$")) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Name must be letters only and not empty.");
            }
        }

        // Price
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Price            : ");
                price = scanner.nextDouble();
                scanner.nextLine();
                validInput = true; // ✅ success
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input! Please enter a number.");
                scanner.nextLine(); // ✅ clear buffer
            }
        }

        // Credit Score
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Credit Score     : ");
                creditScore = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Capacity
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Capacity         : ");
                capacity = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Instructor ID
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Instructor ID    : ");
                instructorId = scanner.nextInt();
                List<InstructorResponseDto> instructor = instructorDao.getInstructorById(instructorId);
                validInput = true;
                if (instructor.isEmpty()) {
                    System.out.println(red + " Instructor with ID " + instructorId + " does not exist!");
                }
            }catch (InputMismatchException e) {
                System.out.println(red+"Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Room
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Room             : ");
            room = scanner.nextLine();
            if (!room.isBlank()) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Room cannot be empty.");
            }
        }

        // Major ID
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Major ID         : ");
                majorId = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Level
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Level            : ");
                level = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }

        // Start Date
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Start Date (yyyy-MM-dd): ");
                startDate = LocalDate.parse(scanner.nextLine().trim());
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println(red+"Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // End Date
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"End Date (yyyy-MM-dd): ");
                endDate = LocalDate.parse(scanner.nextLine().trim());
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println(red+"Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // Create course
        CourseRequestDto request = new CourseRequestDto(
                courseName, price, creditScore, capacity,
                startDate, endDate, instructorId, room, majorId, level
        );
        CourseResponseDto created = courseDao.create(request);
        System.out.println(green+"\n✔ Course created successfully!");
        printCourseTable(List.of(created));
    }


    public void updateCourse(int course_id) {
        boolean validInput;
        System.out.println(cyan+"\n========== Update Course (ID: " + course_id + ") ==========");

        CourseResponseDto current;
        try {
            List<CourseResponseDto> existing = courseDao.getById(course_id);
            if (existing.isEmpty()) {
                System.out.println(red+"No course found with ID: " + course_id);
                return;
            }
            current = existing.get(0);
            System.out.println(cyan+"Current details:");
            printCourseTable(existing);
        } catch (SQLException e) {
            System.err.println(red+"Could not fetch course: " + e.getMessage());
            return;
        }

        System.out.println(cyan+"\nPress Enter to keep the current value, or type a new one:");

        String courseName = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Course Name      [" + current.getCourse_name() + "]: ");
                String courseNameInput = scanner.nextLine().trim();
                courseName = courseNameInput.isEmpty() ? current.getCourse_name() : courseNameInput;
                if (!courseName.isBlank() && courseName.matches("^[a-zA-Z\\s]+$")) {
                    validInput = true;
                } else {
                    System.out.println(red+"Invalid! Name must be letters only and not empty.");
                }
            } catch (Exception e) {
                System.out.println(red+"Invalid input!");
                scanner.nextLine();
            }
        }

        double price = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Price            [" + current.getPrice() + "]: ");
                String priceInput = scanner.nextLine().trim();
                price = priceInput.isEmpty() ? current.getPrice() : Double.parseDouble(priceInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid! Please enter a valid number.");
            }
        }

        int creditScore = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Credit Score     [" + current.getCredit_score() + "]: ");
                String creditInput = scanner.nextLine().trim();
                creditScore = creditInput.isEmpty() ? current.getCredit_score() : Integer.parseInt(creditInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid! Please enter a valid number.");
            }
        }

        int capacity = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Capacity         [" + current.getCapacity() + "]: ");
                String capacityInput = scanner.nextLine().trim();
                capacity = capacityInput.isEmpty() ? current.getCapacity() : Integer.parseInt(capacityInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid! Please enter a valid number.");
            }
        }

        LocalDate startDate = null;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Start Date       [" + current.getStart_date() + "] (yyyy-MM-dd): ");
                String startInput = scanner.nextLine().trim();
                startDate = startInput.isEmpty() ? current.getStart_date() : LocalDate.parse(startInput);
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println(red+"Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        LocalDate endDate = null;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"End Date         [" + current.getEnd_date() + "] (yyyy-MM-dd): ");
                String endInput = scanner.nextLine().trim();
                endDate = endInput.isEmpty() ? current.getEnd_date() : LocalDate.parse(endInput);
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println(red+"Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        int instructorId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Instructor ID    [" + current.getInstructor_id() + "]: ");
                String instructorInput = scanner.nextLine().trim();
                instructorId = instructorInput.isEmpty() ? current.getInstructor_id() : Integer.parseInt(instructorInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid! Please enter a valid number.");
            }
        }

        String room = "";
        validInput = false;
        while (!validInput) {
            System.out.print(yellow+"Room             [" + current.getRoom() + "]: ");
            String roomInput = scanner.nextLine().trim();
            room = roomInput.isEmpty() ? current.getRoom() : roomInput;
            if (!room.isBlank()) {
                validInput = true;
            } else {
                System.out.println(red+"Invalid! Room cannot be empty.");
            }
        }

        int majorId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Major ID         [" + current.getMajor_id() + "]: ");
                String majorInput = scanner.nextLine().trim();
                majorId = majorInput.isEmpty() ? current.getMajor_id() : Integer.parseInt(majorInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid! Please enter a valid number.");
            }
        }

        int level = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Level            [" + current.getLevel() + "]: ");
                String levelInput = scanner.nextLine().trim();
                level = levelInput.isEmpty() ? current.getLevel() : Integer.parseInt(levelInput);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid! Please enter a valid number.");
            }
        }

        try {
            CourseRequestDto request = new CourseRequestDto(
                    courseName, price, creditScore, capacity,
                    startDate, endDate, instructorId, room, majorId, level
            );
            CourseResponseDto updated = courseDao.update(course_id, request);
            System.out.println(green+"\n✔ Course updated successfully!");
            printCourseTable(List.of(updated));
        } catch (SQLException e) {
            System.err.println(red+"update() failed: " + e.getMessage());
        }
    }


    public void deleteCourse(int course_id) {
        System.out.println(cyan+"\n========== Delete Course (ID: " + course_id + ") ==========");

        // Show the course before deleting so the user knows what will be removed
        try {
            List<CourseResponseDto> existing = courseDao.getById(course_id);
            if (existing.isEmpty()) {
                System.out.println(red+"No course found with ID: " + course_id);
                return;
            }
            printCourseTable(existing);
        } catch (InputMismatchException i) {
            System.out.println(red+"Invalid input! Please input number");
        } catch (SQLException e) {
            System.err.println(red+"Could not fetch course: " + e.getMessage());
            return;
        }

        System.out.print(yellow+"Are you sure you want to delete this course? (y/n): ");
        String confirm = scanner.next().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("Delete cancelled.");
            return;
        }

        try {
            boolean deleted = courseDao.delete(course_id);
            if (deleted) {
                System.out.println(green+"✔ Course with ID " + course_id + " deleted successfully.");
            } else {
                System.out.println(red+"✘ Delete failed — no course found with ID: " + course_id);
            }
        } catch (SQLException e) {
            System.err.println(red+"delete() failed: " + e.getMessage());
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
        System.out.println(cyan+"""
                ┌─────────────────────────────────┐
                │     Display Course By           │
                │  1. Major ID                    │
                │  2. Course ID                   │
                │  0. Back                        │
                └─────────────────────────────────┘""");
        try {
            System.out.print(yellow+"Please Enter Option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.print(yellow+"Enter Major ID: ");
                    displaySingleCourseByMajorId(scanner.nextInt());
                }
                case 2 -> {
                    System.out.print(yellow+"Enter Course ID: ");
                    displaySingleCourseByCourseId(scanner.nextInt());
                }
                case 0 ->{return ;}
                default -> {
                    System.out.println(red+"Invalid option. Please enter 1, 2 or 0.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println(red+"Invalid input ! Please input number");
            scanner.nextLine();
        }
        }
    }
}

