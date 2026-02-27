package org.system.view;

import org.system.controller.*;
import org.system.service.CourseService;
import org.system.service.MajorService;

import java.util.Random;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final CourseController courseController = new CourseController();
    private final RoadmapController roadmapController = new RoadmapController();
    private final SubjectController subjectController = new SubjectController();
    private final InstructorController instructorController = new InstructorController();
    private final MajorController majorController = new MajorController();

    public void AdminStart() {
        while (true) {
            System.out.println("""
                    ╔═════════════════════════════════╗
                    ║             ADMIN               ║
                    ╠═════════════════════════════════╣
                    ║ 1. Manage Course                ║
                    ║ 2. Manage Roadmap               ║
                    ║ 3. Manage Instructor            ║
                    ║ 4. Manage Transcript            ║
                    ║ 5. Manage Student               ║
                    ║ 6. Manage Subject               ║
                    ║ 7. Manage Major                 ║
                    ║ 0. Back                         ║
                    ╚═════════════════════════════════╝
                    """);
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
            boolean running = true;

            switch (option) {
                case 1 -> courseMenu();
                case 2 -> roadmapMenu();
                case 3 -> instructorMenu();
                case 4 -> transcriptMenu();
                case 5 -> studentMenu();
                case 6 -> subjectMenu();
                case 7 -> majorMenu();
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

    public void studentMenu() {
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

    public void instructorMenu() {
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
            switch (option) {
                case 1 -> instructorController.displayAllInstructors();
                case 2 -> instructorController.displayInstructorById();
                case 3 -> instructorController.createInstructor();
                case 4 -> instructorController.updateInstructor();
                case 5 -> instructorController.createInstructor();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
        }
    }

    public void transcriptMenu() {
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

    public void subjectMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""
    
    ╔═══════════════════════════════════╗
    ║        SUBJECT MANAGEMENT         ║
    ╠═══════════════════════════════════╣
    ║  1. Display All Subjects          ║
    ║  2. View Subject by ID            ║
    ║  3. Create Subject                ║
    ║  4. Update Subject                ║
    ║  5. Delete Subject                ║
    ║  0. Exit                          ║
    ╚═══════════════════════════════════╝
    """);
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> subjectController.displayAllSubjects();
                case 2 -> subjectController.displaySubjectById();
                case 3 -> subjectController.createSubject();
                case 4 -> subjectController.updateSubject();
                case 5 -> subjectController.deleteSubject();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
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

    public void majorMenu() {
        boolean running = true;

        while (running) {
            System.out.println("""
                    
                    ╔══════════════════════════════════╗
                    ║              MAJOR               ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Major            ║
                    ║  2. Display Major By ID          ║
                    ║  3. Create New Major             ║
                    ║  4. Update Major                 ║
                    ║  5. Delete Major                 ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            System.out.print("Please Enter Option: ");
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> majorController.displayAllMajor();
                case 2 -> majorController.displayMajorById();
                case 3 -> majorController.create();
                case 4 -> majorController.update();
                case 5 -> majorController.delete();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
        }
    }

}