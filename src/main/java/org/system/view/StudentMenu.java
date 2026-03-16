
package org.system.view;

import org.system.controller.CourseController;
import org.system.controller.RoadmapController;
import org.system.controller.SubjectController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final RoadmapController roadmapController = new RoadmapController();
    private final CourseController courseController = new CourseController();
    private final SubjectController subjectController = new SubjectController();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    public void studentStart() {
        while (true) {
            System.out.println(blue+"""
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
        """);
            try{
                System.out.print(yellow+"Enter option : ");
                int choice = scanner.nextInt();
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
                        System.out.println("Ot tn mean te bach jol merl te");
                    }
                    case 4 -> roadmapController.chooseIdOrAllRoadmap();
                    case 5 -> System.out.println("Ot tn mean te bach jol merl te");
                    case 0 -> { return; }
                    default  -> System.out.println(red+"Invalid option.");
                }
            }catch (InputMismatchException e){
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            }
        }
    }

}
