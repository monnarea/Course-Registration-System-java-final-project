package org.system.view;

import org.system.controller.CourseController;
import org.system.controller.RoadmapController;

import java.util.Scanner;

public class StudentMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final RoadmapController roadmapController = new RoadmapController();
    private final CourseController courseController = new CourseController();
    public void studentStart() {
        while (true) {
            System.out.println("""
        ╔═════════════════════════════════╗
        ║   COURSE REGISTRATION SYSTEM    ║
        ╠═════════════════════════════════╣
        ║ 1. Course                       ║
        ║ 2. Enrollment                   ║
        ║ 3. Roadmap                      ║
        ║ 4. Transcript                   ║
        ║ 0. Back                         ║
        ╚═════════════════════════════════╝
        """);
            System.out.print("Enter option : ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    courseController.displayAllCourse();
                    courseController.displayCourseBy();
                }
                case 2 -> {
                    System.out.println("Ot tn mean te bach jol merl te");
                }
                case 3 -> roadmapController.chooseIdOrAllRoadmap();
                case 4 -> {
                    System.out.println("Ot tn mean te bach jol merl te");
                }
                case 0 -> { return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

}
