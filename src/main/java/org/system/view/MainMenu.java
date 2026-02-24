package org.system.view;

import org.system.controller.RoadmapController;
import org.system.service.CourseService;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final RoadmapController roadmapController = new RoadmapController();
    private final CourseService courseService = new CourseService();

    public void start() {
        while (true) {
            System.out.println("""
        ╔═════════════════════════════════╗
        ║   COURSE REGISTRATION SYSTEM    ║
        ╠═════════════════════════════════╣
        ║ 1. Major                        ║
        ║ 2. Course                       ║
        ║ 3. Roadmap                      ║
        ║ 4. Instructor                   ║
        ║ 5. Transcript                   ║
        ║ 6. Student                      ║
        ║ 0. Exit                         ║
        ╚═════════════════════════════════╝
        """);
            System.out.print("Enter option : ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 2 -> courseService.displayAllCourse();
                case 3 -> roadmapController.chooseIdOrAllRoadmap();
                case 0 -> { System.out.println("Goodbye!"); return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

}
