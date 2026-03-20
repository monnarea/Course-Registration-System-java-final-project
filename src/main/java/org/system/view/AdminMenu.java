
package org.system.view;

import org.system.controller.*;
import org.system.exception.EnrollmentException;
import org.system.service.CourseService;
import org.system.service.MajorService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

//import static sun.security.jgss.GSSToken.readInt;

public class AdminMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final CourseController courseController = new CourseController();
    private final RoadmapController roadmapController = new RoadmapController();
    private final SubjectController subjectController = new SubjectController();
    private final InstructorController instructorController = new InstructorController();
    private final MajorController majorController = new MajorController();
    private final CourseTimeController courseTimeController = new CourseTimeController();
    private final EnrollmentController enrollmentController = new EnrollmentController();
    private final StudentController studentController = new StudentController();
    private final TranscriptController transcriptController = new TranscriptController();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    public void AdminStart() {
        while (true) {
            System.out.println(cyan+"""
                    ╔═════════════════════════════════╗
                    ║             ADMIN               ║
                    ╠═════════════════════════════════╣
                    ║ 1. Manage Course                ║
                    ║ 2. Manage Course Time           ║
                    ║ 3. Manage Enrollment            ║
                    ║ 4. Manage Roadmap               ║
                    ║ 5. Manage Instructor            ║
                    ║ 6. Manage Transcript            ║
                    ║ 7. Manage Student               ║
                    ║ 8. Manage Subject               ║
                    ║ 9. Manage Major                 ║
                    ║ 0. Back                         ║
                    ╚═════════════════════════════════╝
                    """);

            try {
                System.out.print(yellow+"Please Enter Option: ");
                int option = scanner.nextInt();

                switch (option) {
                    case 1 -> {
                        courseMenu();
                    }
                    case 2 -> {
                        courseTimeMenu();
                    }
                    case 3 -> {
                        scanner.nextLine();
                        enrollmentMenu() ;
                    }
                    case 4 -> {
                        roadmapMenu();
                    }
                    case 5 -> {
                        instructorMenu();

                    }
                    case 6 -> {
                        transcriptMenu();

                    }
                    case 7 -> {
                        scanner.nextLine();
                        studentMenu();

                    }
                    case 8 -> {
                        subjectMenu();
                    }
                    case 9 -> {
                        majorMenu();

                    }
                    case 0 -> {
                        new MainMenu().start();
                    }
                    default -> System.out.println(red+"Invalid option. Please enter 0–9.");
                }
            }catch (InputMismatchException e){
                System.out.println(red+"Invalid input! Please input number");
                scanner.nextLine();
            }
        }
    }

    public void courseMenu() {
        boolean running = true;

        while (running) {
            System.out.println(cyan+"""
                    
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
            try {
                System.out.print(yellow+"Please Enter Option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1 -> courseController.displayAllCourse();
                    case 2 -> courseController.displayCourseBy();
                    case 3 -> courseController.createCourse();
                    case 4 -> courseController.deleteCourse();
                    case 5 -> courseController.updateCourse();
                    case 0 -> running = false;
                    default -> System.out.println(red+"Invalid option. Please enter 0–5.");
                }
            }catch (InputMismatchException e){
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void courseTimeMenu() {
        boolean running = true;

        while (running) {
            System.out.println(cyan+"""
                    
                    ╔══════════════════════════════════╗
                    ║          COURSE  TIME            ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Courses Time     ║
                    ║  2. Display Course Time By ID    ║
                    ║  3. Create New Course Time       ║
                    ║  4. Update Course Time           ║
                    ║  5. Delete Course Time           ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            try {
                System.out.print(yellow+"Please Enter Option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1 -> courseTimeController.displayAllCourseTime();
                    case 2 -> courseTimeController.displayCourseTimeById();
                    case 3 -> courseTimeController.create();
                    case 4 -> courseTimeController.update();
                    case 5 -> courseTimeController.delete();
                    case 0 -> running = false;
                    default -> System.out.println(red+"Invalid option. Please enter 0–5.");
                }
            }catch (InputMismatchException e){
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void studentMenu() {
        while (true) {
            System.out.println(cyan + """
                
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

            System.out.print(yellow + "Please Enter Option: ");
            String input = scanner.nextLine().trim(); // ✅ avoid empty line crash
            if (input.isEmpty()) continue;            // ✅ skip empty input

            try {
                int option = Integer.parseInt(input);
                switch (option) {
                    case 1 -> studentController.displayAllStudents();
                    case 2 -> studentController.displayStudentById();
                    case 3 -> studentController.createStudent();
                    case 4 -> studentController.updateStudent();
                    case 5 -> studentController.deleteStudent();
                    case 0 -> { return; } // ✅ return to AdminMenu
                    default -> System.out.println(red + "Invalid option. Please enter 0–5.");
                }
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid input! Please enter a number.");
            }
        }
    }

    public void instructorMenu() {
        boolean running = true;
        while (running) {
            System.out.println(cyan+"""
                    
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
            try {
                System.out.print(yellow+"Please Enter Option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1 -> instructorController.displayAllInstructors();
                    case 2 -> instructorController.displayInstructorById();
                    case 3 -> instructorController.createInstructor();
                    case 4 -> instructorController.updateInstructor();
                    case 5 -> instructorController.deleteInstructor();
                    case 0 -> running = false;
                    default -> System.out.println(red+"Invalid option. Please enter 0–5.");
                }
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            }
        }
    }

    public void transcriptMenu() {
        boolean running = true;

        while (running) {
            System.out.println(cyan+"""

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
            System.out.print(yellow+"Please Enter Option: ");
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> transcriptController.displayAll();
                case 2 -> transcriptController.displayById();
                case 3 -> transcriptController.create();
                case 4 -> transcriptController.update();
                case 5 -> transcriptController.delete();
                case 0 -> new AdminMenu();
                default -> System.out.println("Invalid option. Please enter 0–5.");
            }
            return;
        }
    }

    public void subjectMenu() {
        boolean running = true;

        while (running) {
            System.out.println(cyan+"""
    
    ╔═══════════════════════════════════╗
    ║              SUBJECT              ║
    ╠═══════════════════════════════════╣
    ║  1. Display All Subjects          ║
    ║  2. View Subject by ID            ║
    ║  3. Create Subject                ║
    ║  4. Update Subject                ║
    ║  5. Delete Subject                ║
    ║  0. Exit                          ║
    ╚═══════════════════════════════════╝
    """);
            try
            {
                System.out.print(yellow+"Please Enter Option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1 -> subjectController.displayAllSubjects();
                    case 2 -> subjectController.displaySubjectById();
                    case 3 -> subjectController.createSubject();
                    case 4 -> subjectController.updateSubject();
                    case 5 -> subjectController.deleteSubject();
                    case 0 -> running = false;
                    default -> System.out.println(red+"Invalid option. Please enter 0–5.");
                }
            }catch (InputMismatchException e){

                System.out.println(red+"Invalid input! Please input number") ;
                scanner.nextInt();

            }


        }
    }

    public void roadmapMenu() {
        boolean running = true;

        while (running) {
            System.out.println(cyan+"""
                    ╔══════════════════════════════════╗
                    ║             ROADMAP              ║
                    ╠══════════════════════════════════╣
                    ║  1. Display All Roadmap          ║
                    ║  2. Display Roadmap By ID        ║
                    ║  3. Delete Roadmap               ║
                    ║  4. Create Roadmap               ║
                    ║  0. Back                         ║
                    ╚══════════════════════════════════╝""");
            try {
                System.out.print(yellow+"Please Enter Option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1 -> roadmapController.displayAllRoadmap();
                    case 2 -> roadmapController.chooseIdOrAllRoadmap();
                    case 3 -> roadmapController.delete();
                    case 4 -> roadmapController.create();
                    case 0 -> running = false;
                    default -> System.out.println(red+"Invalid option. Please enter 0–5.");
                }
            }catch (InputMismatchException e){
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            }
        }
    }

    public void majorMenu() {
        boolean running = true;

        while (running) {
            System.out.println(cyan+"""
                    
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
            try {
                System.out.print(yellow+"Please Enter Option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1 -> majorController.displayAllMajor();
                    case 2 -> majorController.displayMajorById();
                    case 3 -> majorController.create();
                    case 4 -> majorController.update();
                    case 5 -> majorController.delete();
                    case 0 -> running = false;
                    default -> System.out.println(red+"Invalid option. Please enter 0–5.");
                }
            }catch (InputMismatchException e){
                System.out.println(red+"Invalid input! Please input number");
                scanner.nextLine();
            }
        }

    }
    public void enrollmentMenu() {
        while (true) {
            System.out.println(cyan+"\n╔══════════════════════════════════╗");
            System.out.println(cyan+"║       ENROLLMENT SYSTEM          ║");
            System.out.println(cyan+"╠══════════════════════════════════╣");
            System.out.println(cyan+"║  1. View All Enrollments         ║");
            System.out.println(cyan+"║  2. Find Enrollment by ID        ║");
            System.out.println(cyan+"║  3. Update Enrollment            ║");
            System.out.println(cyan+"║  4. Delete Enrollment            ║");
            System.out.println(cyan+"║  0. Exit to Admin Menu           ║");
            System.out.println(cyan+"╚══════════════════════════════════╝");

            try {
                System.out.print(yellow + "Please Enter Option: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                int option = Integer.parseInt(input);
                switch (option) {
                    case 1 -> enrollmentController.viewAllEnrollments();
                    case 2 -> enrollmentController.findById();
                    case 3 -> enrollmentController.updateEnrollment();
                    case 4 -> enrollmentController.deleteEnrollment();
                    case 0 -> AdminStart();
                    default -> System.out.println(red + "Invalid option. Please enter 0–6.");
                }
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid input! Please enter a number.");
            }
        }
    }
}
