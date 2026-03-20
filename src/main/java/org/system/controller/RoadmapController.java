
package org.system.controller;

import org.system.service.RoadmapService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RoadmapController {


    private final RoadmapService roadmapService = new RoadmapService();
    private final Scanner scanner = new Scanner(System.in);
    private static final String green = "\u001B[32m";
    private static final String blue = "\u001B[34m";
    private static final String yellow = "\u001B[33m";
    private static final String purple = "\u001B[35m";
    private static final String red = "\u001B[31m";
    private static final String cyan = "\u001B[36m";
    private static final String white = "\u001B[37m";


    // ===============================
    // CHOOSE ALL OR SINGLE ROADMAP
    // ===============================
    public void chooseIdOrAllRoadmap() {
        while (true) {
            try {
                System.out.println(cyan + """
                ╔════════╦══════════════════════════════╗
                ║ Option ║ Description                  ║
                ╠════════╩══════════════════════════════╣
                ║   [1]  Show Roadmap By Major Id       ║
                ║   [2]  Show By Road Id                ║
                ║   [0]  Back                           ║
                ╚═══════════════════════════════════════╝
                """);
                System.out.print(yellow+"Enter option: ");
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1 -> displayByMajorId();
                    case 2 -> displayByRoadmapId();
                    case 0 -> { return; }                              // ✅ Now actually exits
                    default -> System.out.println(cyan+"Please enter option (1-2)"); // ✅ No recursion
                }
            } catch (NumberFormatException e) {                        // ✅ Correct exception
                System.out.println(red+"Invalid input! Please input a number");
            }
        }
    }


    public void displayAllRoadmap() {
        roadmapService.displayAllRoadmap();
    }

//    public void displaySingleRoadmap() {
//        while (true) {
//            System.out.println("""
//                    Display one Roadmap by:
//                    1. Roadmap ID
//                    2. Major ID
//                    0. Back
//                    """);
//try {
//    System.out.print("Please Enter Option: ");
//    int option = Integer.parseInt(scanner.nextLine());
//
//    switch (option) {
//        case 1 -> displayByRoadmapId();
//        case 2 -> displayByMajorId();
//        case 0 -> { return; }
//        default -> System.out.println("Invalid option. Please enter 0-2.");
//                    }
//        } catch (NumberFormatException e) {
//    System.out.println("Invalid input! Please input a number");
//            }
//       }
//    }





    private void displayByMajorId() {
        while (true) {
            try {
                System.out.print(yellow+"Enter Major ID: ");
                Integer majorId = Integer.parseInt(scanner.nextLine());
                roadmapService.displaySingleRoadmap(majorId);
                return;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid input ! please input number");
            }
        }
    }



    private void displayByRoadmapId() {
        while (true) {
            try {
                System.out.print(yellow+"Enter Roadmap ID: ");
                Integer roadmapId = Integer.parseInt(scanner.nextLine());
                roadmapService.displayRoadmapById(roadmapId);
                return;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid input ! please input number");
            }
        }
    }



    public void create() {
        roadmapService.createRoadmap();
    }

    public void update() {
        boolean validInput;

        // ── Roadmap ID ───────────────────────────────────────────
        int id = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Enter roadmap_id to update : ");
                id = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a valid number.");
            }
        }

        roadmapService.updateRoadmap(id);
    }


    public void delete() {
        while (true) {
            try {
                System.out.print(yellow+"Enter Roadmap ID to delete: ");
                var roadmapId = Integer.parseInt(scanner.nextLine());
                roadmapService.deleteRoadmap(roadmapId);
                return;
            } catch (NumberFormatException e) {
                System.out.println(red+"Invalid input ! please input number");
            }
        }
    }
}
