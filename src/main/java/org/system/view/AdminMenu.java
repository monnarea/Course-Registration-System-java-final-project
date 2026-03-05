package org.system.view;

import org.system.controller.CourseController;
import org.system.controller.RoadmapController;
import org.system.controller.SubjectController;
import org.system.service.CourseService;

import java.util.Random;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final CourseController courseController = new CourseController();
    private final RoadmapController roadmapController = new RoadmapController();
    private final SubjectController subjectController = new SubjectController();

    public void AdminStart() {
        while (true) {
            System.out.println("""
                    ╔═════════════════════════════════╗
                    ║             ADMIN               ║
                    ╠═════════════════════════════════╣
                    ║ 1. Manage Course                ║
                    ║ 2. Manage Roadmap               ║
                    ║ 3. Manage Enrollment            ║
                    ║ 4. Manage Instructor            ║
                    ║ 5. Manage Transcript            ║
                    ║ 6. Manage Student               ║
                    ║ 7. Manage Subject               ║
                    ║ 0. Back                         ║
                    ╚═════════════════════════════════╝
                    """);
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
            boolean running = true;

            switch (option) {
                case 1 -> courseMenu();
                case 2 -> roadmapMenu();
                case 3 -> InstructorMenu();
                case 4 -> TranscriptMenu();
                case 5 -> StudentMenu();
                case 7 -> SubjectMenu();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
        }
    }

    public void courseMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""
                    
                    ╔══════════════════════════════════╗
                    ║              COURSE              ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Courses          ║
                    ║  2. Display Course By ID         ║
                    ║  3. Create New Course            ║
                    ║  4. Update Course                ║
                    ║  5. Delete Course                ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> courseController.displayAllCourse();
                case 2 -> courseController.displayCourseBy();
                case 3 -> courseController.createCourse();
                case 4 -> courseController.deleteCourse();
                case 5 -> courseController.updateCourse();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
        }
    }

    public void StudentMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""
                    
                    ╔══════════════════════════════════╗
                    ║             STUDENT              ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Student          ║
                    ║  2. Display Student By ID        ║
                    ║  3. Create New Student           ║
                    ║  4. Update Student               ║
                    ║  5. Delete Student               ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
//            switch (option) {
//                case 1 -> courseService.displayAllCourse();
//                case 2 -> courseService.displayCourseById();
//                case 3 -> courseService.createCourse();
//                case 4 -> courseService.deleteCourse(scanner.nextInt());
//                case 5 -> courseService.updateCourse(scanner.nextInt());
//                case 0 -> running = false;
//                default -> System.out.println("Invalid option. Please enter 0–5.");
        }
        return;
    }


    public void InstructorMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""
                    
                    ╔══════════════════════════════════╗
                    ║            INSTRUCTOR            ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Instructors      ║
                    ║  2. Display Instructors By ID    ║
                    ║  3. Create New Instructors       ║
                    ║  4. Update Instructors           ║
                    ║  5. Delete Instructors           ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
//            switch (option) {
//                case 1 -> courseService.displayAllCourse();
//                case 2 -> courseService.displayCourseById();
//                case 3 -> courseService.createCourse();
//                case 4 -> courseService.deleteCourse(scanner.nextInt());
//                case 5 -> courseService.updateCourse(scanner.nextInt());
//                case 0 -> running = false;
//                default -> System.out.println("Invalid option. Please enter 0–5.");
//            }
            return;
        }
    }

    public void TranscriptMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""

                    ╔══════════════════════════════════╗
                    ║            TRANSCRIPT            ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Transcript       ║
                    ║  2. Display Transcript By ID     ║
                    ║  3. Create New Transcript        ║
                    ║  4. Update Transcript            ║
                    ║  5. Delete Transcript            ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
//            switch (option) {
//                case 1 -> courseService.displayAllCourse();
//                case 2 -> courseService.displayCourseById();
//                case 3 -> courseService.createCourse();
//                case 4 -> courseService.deleteCourse(scanner.nextInt());
//                case 5 -> courseService.updateCourse(scanner.nextInt());
//                case 0 -> running = false;
//                default -> System.out.println("Invalid option. Please enter 0–5.");
//            }
            return;
        }
    }

    public void SubjectMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""
    
    ╔══════════════════════════════╗
    ║      SUBJECT MANAGEMENT      ║
    ╠══════════════════════════════╣
    ║  1. Create Subject           ║
    ║  2. View All Subjects        ║
    ║  3. View Subject by ID       ║
    ║  4. View Subjects by Course  ║
    ║  5. Update Subject           ║
    ║  6. Delete Subject           ║
    ║  0. Exit                     ║
    ╚══════════════════════════════╝
    """);
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
            switch (option) {
//                case 1 -> subjectController.displayAllSubjects();
//                case 2 -> courseService.displayCourseById();
//                case 3 -> courseService.createCourse();
//                case 4 -> courseService.deleteCourse(scanner.nextInt());
//                case 5 -> courseService.updateCourse(scanner.nextInt());
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
            return;
        }
    }

    public void roadmapMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""
                    ╔══════════════════════════════════╗
                    ║             ROADMAP              ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Roadmap          ║
                    ║  2. Display Roadmap By ID        ║
                    ║  3. Delete Roadmap               ║
                    ║  4. Create Roadmap               ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> roadmapController.displayAllRoadmap();
                case 2 -> roadmapController.displaySingleRoadmap();
                case 3 -> roadmapController.delete();
                case 4 -> roadmapController.create();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
        }
    }
}