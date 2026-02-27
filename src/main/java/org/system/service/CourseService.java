package org.system.service;

import org.system.model.dao.CourseDaoImpl;
import org.system.model.dto.request.CourseRequestDto;
import org.system.model.dto.response.CourseResponseDto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printCourseTable;
import static org.system.view.View.printSingleCourseTable;

public class CourseService {

    private final CourseDaoImpl courseDao = new CourseDaoImpl();
    private final CourseDaoImpl singleCourseDao = new CourseDaoImpl();
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void displayAllCourse(){

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

    public void displaySingleCourseByCourseId(int course_id){

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

    public void displaySingleCourseByMajorId(int major_id){
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

    public void createCourse() {
        System.out.println("\n========== Create New Course ==========");

        try {
            System.out.print("Course Name      : ");
            // flush
            String courseName = scanner.nextLine();

            System.out.print("Price            : ");
            double price = scanner.nextDouble();

            System.out.print("Credit Score     : ");
            int creditScore = scanner.nextInt();

            System.out.print("Capacity         : ");
            int capacity = scanner.nextInt();

            System.out.print("Start Date (yyyy-MM-dd): ");
            LocalDate startDate = parseDate(scanner.next());

            System.out.print("End Date   (yyyy-MM-dd): ");
            LocalDate endDate = parseDate(scanner.next());

            System.out.print("Instructor ID    : ");
            int instructorId = scanner.nextInt();

            System.out.print("Room             : ");
//            String room = scanner.nextLine();
            String room = scanner.next();

            System.out.print("Major ID         : ");
            int majorId = scanner.nextInt();

            System.out.print("Level            : ");
            int level = scanner.nextInt();

            CourseRequestDto request = new CourseRequestDto(
                    courseName, price, creditScore, capacity,
                    startDate, endDate, instructorId, room, majorId, level
            );

            CourseResponseDto created = courseDao.create(request);
            System.out.println("\n✔ Course created successfully!");
            printCourseTable(List.of(created));

        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Please use yyyy-MM-dd.");
        } catch (SQLException e) {
            System.err.println("create() failed: " + e.getMessage());
        }
        System.out.println();
    }


    public void updateCourse(int course_id) {
        System.out.println("\n========== Update Course (ID: " + course_id + ") ==========");

        // Step 1: Fetch existing course
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

        try {
            // Flush any leftover newline
//            if (scanner.hasNextLine()) scanner.nextLine();
            // ── Course Name ──────────────────────────────────────────
            System.out.print("Course Name      [" + current.getCourse_name() + "]: ");
            String courseNameInput = scanner.nextLine().trim();
            String courseName = courseNameInput.isEmpty() ? current.getCourse_name() : courseNameInput;

            // ── Price ────────────────────────────────────────────────
            System.out.print("Price            [" + current.getPrice() + "]: ");
            String priceInput = scanner.nextLine().trim();
            double price = priceInput.isEmpty() ? current.getPrice() : Double.parseDouble(priceInput);

            // ── Credit Score ─────────────────────────────────────────
            System.out.print("Credit Score     [" + current.getCredit_score() + "]: ");
            String creditInput = scanner.nextLine().trim();
            int creditScore = creditInput.isEmpty() ? current.getCredit_score() : Integer.parseInt(creditInput);

            // ── Capacity ─────────────────────────────────────────────
            System.out.print("Capacity         [" + current.getCapacity() + "]: ");
            String capacityInput = scanner.nextLine().trim();
            int capacity = capacityInput.isEmpty() ? current.getCapacity() : Integer.parseInt(capacityInput);

            // ── Start Date ───────────────────────────────────────────
            System.out.print("Start Date       [" + current.getStart_date() + "] (yyyy-MM-dd): ");
            String startInput = scanner.nextLine().trim();
            LocalDate startDate = startInput.isEmpty() ? current.getStart_date() : parseDate(startInput);

            // ── End Date ─────────────────────────────────────────────
            System.out.print("End Date         [" + current.getEnd_date() + "] (yyyy-MM-dd): ");
            String endInput = scanner.nextLine().trim();
            LocalDate endDate = endInput.isEmpty() ? current.getEnd_date() : parseDate(endInput);

            // ── Instructor ID ────────────────────────────────────────
            System.out.print("Instructor ID    [" + current.getInstructor_id() + "]: ");
            String instructorInput = scanner.nextLine().trim();
            int instructorId = instructorInput.isEmpty() ? current.getInstructor_id() : Integer.parseInt(instructorInput);

            // ── Room ─────────────────────────────────────────────────
            System.out.print("Room             [" + current.getRoom() + "]: ");
            String roomInput = scanner.nextLine().trim();
            String room = roomInput.isEmpty() ? current.getRoom() : roomInput;

            // ── Major ID ─────────────────────────────────────────────
            System.out.print("Major ID         [" + current.getMajor_id() + "]: ");
            String majorInput = scanner.nextLine().trim();
            int majorId = majorInput.isEmpty() ? current.getMajor_id() : Integer.parseInt(majorInput);

            // ── Level ────────────────────────────────────────────────
            System.out.print("Level            [" + current.getLevel() + "]: ");
            String levelInput = scanner.nextLine().trim();
            int level = levelInput.isEmpty() ? current.getLevel() : Integer.parseInt(levelInput);

            // Step 2: Build request with final values (mixed old + new)
            CourseRequestDto request = new CourseRequestDto(
                    courseName, price, creditScore, capacity,
                    startDate, endDate, instructorId, room, majorId, level
            );

            // Step 3: Send to DAO
            CourseResponseDto updated = courseDao.update(course_id, request);
            System.out.println("\n✔ Course updated successfully!");
            printCourseTable(List.of(updated));

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format entered: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Please use yyyy-MM-dd.");
        } catch (SQLException e) {
            System.err.println("update() failed: " + e.getMessage());
        }

        System.out.println();
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
        System.out.println("""
                ┌─────────────────────────────────┐
                │     Display Course By           │
                │  1. Major ID                    │
                │  2. Course ID                   │
                └─────────────────────────────────┘""");
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
            default -> {
                System.out.println("Invalid option. Please enter 1 or 2.");
                displayCourseById();
            }
        }
    }
}
