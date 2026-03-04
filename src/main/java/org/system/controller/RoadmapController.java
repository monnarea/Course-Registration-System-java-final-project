package org.system.controller;

import org.system.service.RoadmapService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RoadmapController {

    private final RoadmapService roadmapService = new RoadmapService();
    private final Scanner scanner = new Scanner(System.in);

    // ===============================
    // CHOOSE ALL OR SINGLE ROADMAP
    // ===============================
    public void chooseIdOrAllRoadmap() {
        while (true) {
            try {
                System.out.println("""
                Choose roadmap:
                1. Show All Roadmap
                2. Show Roadmap By Major Is
                3. Show By Road Id
                0. Back
                """);
                System.out.print("Enter option: ");
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1 -> displayAllRoadmap();
                    case 2 -> displayByMajorId();
                    case 3 -> displayByRoadmapId();
                    case 0 -> { return; }                              // ✅ Now actually exits
                    default -> System.out.println("Please enter option (1-2)"); // ✅ No recursion
                }
            } catch (NumberFormatException e) {                        // ✅ Correct exception
                System.out.println("Invalid input! Please input a number");
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
                System.out.print("Enter Major ID: ");
                Integer majorId = Integer.parseInt(scanner.nextLine());
                roadmapService.displaySingleRoadmap(majorId);
                return;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input ! please input number");
            }
        }
    }



    private void displayByRoadmapId() {
        while (true) {
            try {
                System.out.print("Enter Roadmap ID: ");
                Integer roadmapId = Integer.parseInt(scanner.nextLine());
                roadmapService.displayRoadmapById(roadmapId);
                return;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input ! please input number");
            }
        }
    }



    public void create() {
        roadmapService.createRoadmap();
    }



    public void delete() {
        while (true) {
            try {
                System.out.print("Enter Roadmap ID to delete: ");
                var roadmapId = Integer.parseInt(scanner.nextLine());
                roadmapService.deleteRoadmap(roadmapId);
                return;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input ! please input number");
            }
        }
    }
}