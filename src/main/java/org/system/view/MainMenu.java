package org.system.view;

import org.system.controller.RoadmapController;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final RoadmapController roadmapController = new RoadmapController();

    public void start() {
            System.out.println("╔═════════════════════════════════╗");
            System.out.println("║   COURSE REGISTRATION SYSTEM    ║");
            System.out.println("╚═════════════════════════════════╝");

        while (true) {
            System.out.println("""
                1. Major
                2. Course
                3. Roadmap
                4. Instructor
                5. Transcript
                6. Student
                0. Exit
                """);
            System.out.print("Enter option : ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 3 ->roadmapController.chooseIdOrAllRoadmap();
                case 0 -> { System.out.println("Goodbye!"); return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

}
