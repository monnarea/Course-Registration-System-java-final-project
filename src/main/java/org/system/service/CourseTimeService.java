package org.system.service;

import org.system.model.dao.CourseTimeDaoImpl;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.CourseTimeResponseDto;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.*;
import org.system.util.Pagination;

public class CourseTimeService {
    private final Scanner scanner = new Scanner(System.in);
    private final CourseTimeDaoImpl courseTimeDao = new CourseTimeDaoImpl();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";
    public void displayAllCourseTime() {

        try {
            List<CourseTimeResponseDto> allCourses = courseTimeDao.getAll();

            if (allCourses.isEmpty()) {

                System.out.println(red+"No courses found in database.");
            } else {
                System.out.println(cyan+"Total courses Time found: " + allCourses.size());

                printCourseTimeTablePaginated(allCourses);

            }
        } catch (SQLException e) {
            System.err.println(red+"getAll() failed: " + e.getMessage());
        }

        System.out.println();

    }

    public void displayCourseTimeById(int id) {
        try {
            List<CourseTimeResponseDto> result = courseTimeDao.getById(id);
            if (result.isEmpty()) {
                System.out.println(result+"No course Time found.");
            } else {
                printCourseTimeTable(result); // reuse same printTable method
            }
        } catch (SQLException e) {
            System.err.println(red+"getById() failed: " + e.getMessage());
        }


    }

    public void createCourseTime() throws SQLException {
        System.out.println(cyan+"\n========== Create New Course Time ==========");

        // Declare all variables up front
        Integer courseId = 0;
        String day_of_week = "";
        String morning = null;
        String afternoon = null;
        String evening = null;
        boolean validInput;

        // Course Name
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Course Id: ");
                courseId = scanner.nextInt();
                validInput = true;
            }catch (InputMismatchException e ){
                System.out.println(red+"Invalid input! Please input number");
            }

        }
        while (day_of_week.trim().isEmpty()) {
            System.out.print(yellow+"Day of week: ");
            day_of_week = scanner.nextLine();

            if (day_of_week.trim().isEmpty()) {
                System.out.println(red+"Day of week is required! Please enter again.");
            }
        }
        try {
            System.out.print(yellow+"Morning (Press Enter if null): ");
            morning = scanner.nextLine();

            if (morning.trim().isEmpty()) {
                morning = null;   // This will be stored as NULL in database
            }

        } catch (Exception e) {
            System.out.println(yellow+"Error while reading input.");
            morning = null;
        }
        try {
            System.out.print(yellow+"Afternoon (Press Enter if null): ");
            afternoon = scanner.nextLine();

            if (afternoon.trim().isEmpty()) {
                afternoon = null;   // This will be stored as NULL in database
            }

        } catch (Exception e) {
            System.out.println(red+"Error while reading input.");
            afternoon = null;
        }

        try {
            System.out.print(yellow+"Evening (Press Enter if null): ");
            evening = scanner.nextLine();

            if (evening.trim().isEmpty()) {
                evening = null;   // This will be stored as NULL in database
            }

        } catch (Exception e) {
            System.out.println(red+"Error while reading input.");
            evening = null;
        }





        // Create course Time
        CourseTimeResponseDto request = new CourseTimeResponseDto(
                courseId , day_of_week,morning,afternoon,evening
        );
        CourseTimeResponseDto created = courseTimeDao.create(request);
        System.out.println(green+"\n✔ Course Time created successfully!");
        printCourseTimeTable(List.of(created));
    }


    public void updateCourseTime(int id) {
        boolean validInput;
        System.out.println(cyan+"\n========== Update Course Time (ID: " + id + ") ==========");

        CourseTimeResponseDto current;
        try {
            List<CourseTimeResponseDto> existing = courseTimeDao.getById(id);
            if (existing.isEmpty()) {
                System.out.println(yellow+"No course Time found with ID: " + id);
                return;
            }
            current = existing.get(0);
            System.out.println(yellow+"Current details:");
            printCourseTimeTable(existing);
        } catch (SQLException e) {
            System.err.println(red+"Could not fetch course Time: " + e.getMessage());
            return;
        }

        System.out.println(cyan+"\nPress Enter to keep the current value, or type a new one:");

        Integer course_id = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Course Id      [" + current.getCourse_id() + "]: ");
                String courseIdInput = scanner.nextLine().trim();
                course_id = courseIdInput.isEmpty() ? current.getCourse_id() : Integer.parseInt(courseIdInput);
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid input! Please input number");
            }
        }

        String day_of_week = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Day of week     [" + current.getDay_of_week() + "]: ");
                String DayOfWeekInput = scanner.nextLine().trim();
                day_of_week = DayOfWeekInput.isEmpty() ? current.getDay_of_week() : DayOfWeekInput;
                if (!day_of_week.isBlank() ) {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println(red+"Invalid input!");
                scanner.nextLine();
            }
        }

        String morning = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Morning     [" + current.getMorning() + "]: ");
                String MorningInput = scanner.nextLine().trim();
                morning = MorningInput.isEmpty() ? current.getMorning() : MorningInput;
                if (!morning.isBlank() ) {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println(red+"Invalid input!");
                scanner.nextLine();
            }
        }
        String afternoon = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Afternoon     [" + current.getAfternoon() + "]: ");
                String EveningInput = scanner.nextLine().trim();
                afternoon = EveningInput.isEmpty() ? current.getAfternoon(): EveningInput;
                if (!afternoon.isBlank() ) {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println(red+"Invalid input!");
                scanner.nextLine();
            }
        }
        String evening = "";
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow+"Evening     [" + current.getEvening() + "]: ");
                String EveningInput = scanner.nextLine().trim();
                evening = EveningInput.isEmpty() ? current.getEvening() : EveningInput;
                if (!evening.isBlank() ) {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println(red+"Invalid input!");
                scanner.nextLine();
            }
        }

    }


    public void deleteCourseTime(int id) {
        System.out.println(cyan+"\n========== Delete Course Time (ID: " + id + ") ==========");

        // Show the course before deleting so the user knows what will be removed
        try {
            List<CourseTimeResponseDto> existing = courseTimeDao.getById(id);
            if (existing.isEmpty()) {
                System.out.println(red+"No course Time found with ID: " + id);
                return;
            }
            printCourseTimeTable(existing);
        } catch (InputMismatchException i) {
            System.out.println(red+"Invalid input! Please input number");
        } catch (SQLException e) {
            System.err.println(red+"Could not fetch course: " + e.getMessage());
            return;
        }

        System.out.print(cyan+"Are you sure you want to delete this course Time? (y/n): ");
        String confirm = scanner.next().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println(red+"Delete cancelled.");
            return;
        }

        try {
            boolean deleted = courseTimeDao.delete(id);
            if (deleted) {
                System.out.println(green+"✔ Course Time with ID " + id + " deleted successfully.");
            } else {
                System.out.println(red+"✘ Delete failed — no course Time found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println(red+"delete() failed: " + e.getMessage());
        }
        System.out.println();
    }
}