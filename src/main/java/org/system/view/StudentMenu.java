package org.system.view;

import org.system.config.TelegramBotConfig;
import org.system.controller.CourseController;
import org.system.controller.RoadmapController;
import org.system.controller.SubjectController;
import org.system.controller.TranscriptController;
import org.system.service.StudentService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentMenu {
    private final Scanner            scanner             = new Scanner(System.in);
    private final RoadmapController  roadmapController   = new RoadmapController();
    private final CourseController   courseController    = new CourseController();
    private final SubjectController  subjectController   = new SubjectController();
    private final StudentService     studentService      = new StudentService();
    private final TranscriptController transcriptController = new TranscriptController();

    // ── ANSI colors ───────────────────────────────────────────────────────────
    public static final String green  = "\u001B[32m";
    public static final String blue   = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red    = "\u001B[31m";
    public static final String cyan   = "\u001B[36m";
    public static final String white  = "\u001B[37m";
    public static final String reset  = "\u001B[0m";

    public void studentStart() {
        while (true) {
            System.out.println(blue + """
        ╔═════════════════════════════════╗
        ║   COURSE REGISTRATION SYSTEM    ║
        ╠═════════════════════════════════╣
        ║ 1. Course                       ║
        ║ 2. Subject In Course            ║
        ║ 3. Enrollment                   ║
        ║ 4. Roadmap                      ║
        ║ 5. Transcript                   ║
        ║ 0. Back                         ║
        ╚═════════════════════════════════╝
        """ + reset);

            try {
                System.out.print(yellow + "Enter option : " + reset);
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> {
                        courseController.displayAllCourse();
                        courseController.displayCourseBy();
                    }
                    case 2 -> {
                        subjectController.displayAllSubjects();
                        subjectController.displaySubjectById();
                    }
                    case 3 -> {
                        // 1. Register student → get their ID from PostgreSQL
                        int studentId = studentService.createStudent();

                        if (studentId > 0) {
                            // 2. Launch bot with that ID — enrollments will be saved correctly
                            TelegramBotConfig.startBot(studentId);
                        } else {
                            System.out.println(red + "Student creation failed. Cannot start enrollment." + reset);
                        }
                    }
                    case 4 -> roadmapController.chooseIdOrAllRoadmap();
                    case 5 -> {
                        System.out.println(green + "This is the format of Transcript" + reset);
                        transcriptController.format(2);
                    }
                    case 0 -> {
                        new MainMenu().start();
                        return;
                    }
                    default -> System.out.println(red + "Invalid option." + reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid input! Please enter a number." + reset);
            } catch (Exception e) {
                System.out.println(red + "Unexpected error: " + e.getMessage() + reset);
                e.printStackTrace();
            }
        }
    }
}