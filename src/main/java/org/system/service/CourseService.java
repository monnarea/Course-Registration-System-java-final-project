package org.system.service;

import org.system.model.dao.CourseDaoImpl;
import org.system.model.dao.InstructorDaoImpl;
import org.system.model.dto.request.CourseRequestDto;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.InstructorResponseDto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printCourseTable;
import static org.system.view.View.printSingleCourseTable;

public class CourseService {

    private final CourseDaoImpl    courseDao     = new CourseDaoImpl();
    private final InstructorDaoImpl instructorDao = new InstructorDaoImpl();
    private final Scanner          scanner       = new Scanner(System.in);

    public static final String green  = "\u001B[32m";
    public static final String blue   = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red    = "\u001B[31m";
    public static final String cyan   = "\u001B[36m";
    public static final String white  = "\u001B[37m";
    public static final String reset  = "\u001B[0m";

    // ══════════════════════════════════════════════
    // DISPLAY — all
    // ══════════════════════════════════════════════
    public void displayAllCourse() {
        try {
            List<CourseResponseDto> all = courseDao.getAll();
            if (all.isEmpty()) {
                System.out.println(red + "No courses found in database.");
            } else {
                System.out.println(cyan + "Total courses found: " + all.size() + reset);
                printCourseTable(all);
            }
        } catch (SQLException e) {
            System.err.println(red + "getAll() failed: " + e.getMessage());
        }
        System.out.println();
    }

    // ══════════════════════════════════════════════
    // DISPLAY — by course_id
    // ══════════════════════════════════════════════
    public void displaySingleCourseByCourseId(int course_id) {
        try {
            List<CourseResponseDto> courses = courseDao.getById(course_id);
            if (courses.isEmpty()) {
                System.out.println(red + "No course found with ID: " + course_id);
            } else {
                System.out.println(cyan + "Total courses found: " + courses.size());
                printCourseTable(courses);
            }
        } catch (SQLException e) {
            System.err.println(red + "getById() failed: " + e.getMessage());
        }
        System.out.println();
    }

    // ══════════════════════════════════════════════
    // DISPLAY — by major_id
    // ══════════════════════════════════════════════
    public void displaySingleCourseByMajorId(int major_id) {
        try {
            List<CourseResponseDto> result = courseDao.getByMajorId(major_id);
            if (result.isEmpty()) {
                System.out.println(red + "No courses found for Major ID: " + major_id);
            } else {
                printSingleCourseTable(result);
            }
        } catch (SQLException e) {
            System.err.println(red + "getByMajorId() failed: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════
    // CREATE
    // ══════════════════════════════════════════════
    public void createCourse() throws SQLException {
        System.out.println(cyan + "\n========== Create New Course ==========");

        String    courseName  = readCourseName(null);
        double    price       = readDouble("Price            ", -1);
        double    discount    = readDiscount(-1);
        int       creditScore = readInt("Credit Score     ", -1);
        int       capacity    = readInt("Capacity         ", -1);
        int       instructorId= readInstructorId(-1);
        String    room        = readRoom(null);
        int       majorId     = readInt("Major ID         ", -1);
        int       level       = readInt("Level            ", -1);
        LocalDate startDate   = readDate("Start Date (yyyy-MM-dd)", null);
        LocalDate endDate     = readDate("End Date   (yyyy-MM-dd)", null);

        CourseRequestDto req = new CourseRequestDto(
                courseName, price, discount, creditScore, capacity,
                startDate, endDate, instructorId, room, majorId, level
        );
        CourseResponseDto created = courseDao.create(req);
        System.out.println(green + "\n✔ Course created successfully!");
        printCourseTable(List.of(created));
    }

    // ══════════════════════════════════════════════
    // UPDATE
    // ══════════════════════════════════════════════
    public void updateCourse(int course_id) {
        System.out.println(cyan + "\n========== Update Course (ID: " + course_id + ") ==========");

        CourseResponseDto current;
        try {
            List<CourseResponseDto> existing = courseDao.getById(course_id);
            if (existing.isEmpty()) {
                System.out.println(red + "No course found with ID: " + course_id);
                return;
            }
            current = existing.get(0);
            System.out.println(cyan + "Current details:");
            printCourseTable(existing);
        } catch (SQLException e) {
            System.err.println(red + "Could not fetch course: " + e.getMessage());
            return;
        }

        System.out.println(cyan + "\nPress Enter to keep the current value, or type a new one:");

        String    courseName   = readCourseName(current.getCourse_name());
        double    price        = readDouble("Price            ", current.getPrice());
        double    discount     = readDiscount(current.getDiscount());
        int       creditScore  = readInt("Credit Score     ", current.getCredit_score());
        int       capacity     = readInt("Capacity         ", current.getCapacity());
        LocalDate startDate    = readDate("Start Date (yyyy-MM-dd)", current.getStart_date());
        LocalDate endDate      = readDate("End Date   (yyyy-MM-dd)", current.getEnd_date());
        int       instructorId = readInstructorId(current.getInstructor_id());
        String    room         = readRoom(current.getRoom());
        int       majorId      = readInt("Major ID         ", current.getMajor_id());
        int       level        = readInt("Level            ", current.getLevel());

        try {
            CourseRequestDto req = new CourseRequestDto(
                    courseName, price, discount, creditScore, capacity,
                    startDate, endDate, instructorId, room, majorId, level
            );
            CourseResponseDto updated = courseDao.update(course_id, req);
            System.out.println(green + "\n✔ Course updated successfully!");
            printCourseTable(List.of(updated));
        } catch (SQLException e) {
            System.err.println(red + "update() failed: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════
    // DELETE
    // ══════════════════════════════════════════════
    public void deleteCourse(int course_id) {
        System.out.println(cyan + "\n========== Delete Course (ID: " + course_id + ") ==========");

        try {
            List<CourseResponseDto> existing = courseDao.getById(course_id);
            if (existing.isEmpty()) {
                System.out.println(red + "No course found with ID: " + course_id);
                return;
            }
            printCourseTable(existing);
        } catch (SQLException e) {
            System.err.println(red + "Could not fetch course: " + e.getMessage());
            return;
        }

        System.out.print(yellow + "Are you sure you want to delete this course? (y/n): ");
        String confirm = scanner.next().trim().toLowerCase();
        scanner.nextLine();

        if (!confirm.equals("y")) {
            System.out.println("Delete cancelled.");
            return;
        }

        try {
            boolean deleted = courseDao.delete(course_id);
            if (deleted) {
                System.out.println(green + "✔ Course with ID " + course_id + " deleted successfully.");
            } else {
                System.out.println(red + "✘ Delete failed — no course found with ID: " + course_id);
            }
        } catch (SQLException e) {
            System.err.println(red + "delete() failed: " + e.getMessage());
        }
        System.out.println();
    }

    // ══════════════════════════════════════════════
    // DISPLAY MENU (by major / by course)
    // ══════════════════════════════════════════════
    public void displayCourseById() {
        while (true) {
            System.out.println(cyan + """
                    ┌─────────────────────────────────┐
                    │     Display Course By           │
                    │  1. Major ID                    │
                    │  2. Course ID                   │
                    │  0. Back                        │
                    └─────────────────────────────────┘""");
            try {
                System.out.print(yellow + "Please Enter Option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> {
                        System.out.print(yellow + "Enter Major ID: ");
                        displaySingleCourseByMajorId(scanner.nextInt());
                        scanner.nextLine();
                    }
                    case 2 -> {
                        System.out.print(yellow + "Enter Course ID: ");
                        displaySingleCourseByCourseId(scanner.nextInt());
                        scanner.nextLine();
                    }
                    case 0 -> { return; }
                    default -> System.out.println(red + "Invalid option. Please enter 1, 2 or 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println(red + "Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    // ══════════════════════════════════════════════
    // PRIVATE HELPERS — clean reusable input readers
    // ══════════════════════════════════════════════

    /** Pass null / -1 as 'current' when creating (no default to show). */
    private String readCourseName(String current) {
        while (true) {
            String prompt = current == null
                    ? yellow + "Course Name      : "
                    : yellow + "Course Name      [" + current + "]: ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            String value = (input.isEmpty() && current != null) ? current : input;
            if (!value.isBlank() && value.matches("^[a-zA-Z\\s+#.()/-]+$")) return value;
            System.out.println(red + "Invalid! Letters, spaces and basic punctuation only.");
        }
    }

    private double readDouble(String label, double current) {
        while (true) {
            String prompt = current < 0
                    ? yellow + label + ": "
                    : yellow + label + " [" + current + "]: ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && current >= 0) return current;
            try { return Double.parseDouble(input); }
            catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a number.");
            }
        }
    }

    /**
     * Reads discount: only 20 or 50 are accepted.
     * Pass -1 as current when creating (no default).
     */
    private double readDiscount(double current) {
        while (true) {
            String prompt = current < 0
                    ? yellow + "Discount % (20 / 50): "
                    : yellow + "Discount % (20 / 50) [" + (int) current + "]: ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && current >= 0) return current;
            try {
                double d = Double.parseDouble(input);
                if (d == 20 || d == 50) return d;
                System.out.println(red + "Only 20 or 50 are valid discount values.");
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter 20 or 50.");
            }
        }
    }

    private int readInt(String label, int current) {
        while (true) {
            String prompt = current < 0
                    ? yellow + label + ": "
                    : yellow + label + " [" + current + "]: ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && current >= 0) return current;
            try { return Integer.parseInt(input); }
            catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a whole number.");
            }
        }
    }

    private int readInstructorId(int current) {
        while (true) {
            String prompt = current < 0
                    ? yellow + "Instructor ID    : "
                    : yellow + "Instructor ID    [" + current + "]: ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && current >= 0) return current;
            try {
                int id = Integer.parseInt(input);
                List<InstructorResponseDto> inst = instructorDao.getInstructorById(id);
                if (inst.isEmpty()) System.out.println(red + "Instructor ID " + id + " does not exist!");
                else return id;
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a number.");
            }
        }
    }

    private String readRoom(String current) {
        while (true) {
            String prompt = current == null
                    ? yellow + "Room             : "
                    : yellow + "Room             [" + current + "]: ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            String value = (input.isEmpty() && current != null) ? current : input;
            if (!value.isBlank()) return value;
            System.out.println(red + "Invalid! Room cannot be empty.");
        }
    }

    private LocalDate readDate(String label, LocalDate current) {
        while (true) {
            String prompt = current == null
                    ? yellow + label + ": "
                    : yellow + label + " [" + current + "]: ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && current != null) return current;
            try { return LocalDate.parse(input); }
            catch (DateTimeParseException e) {
                System.out.println(red + "Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }
}