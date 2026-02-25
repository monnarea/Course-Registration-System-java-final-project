package org.system.controller;

import org.system.service.RoadmapService;

import java.util.Scanner;

public class RoadmapController {

    private final RoadmapService roadmapService = new RoadmapService();
    private final Scanner scanner = new Scanner(System.in);

    // ===============================
    // CHOOSE ALL OR SINGLE ROADMAP
    // ===============================
    public void chooseIdOrAllRoadmap() {
        while (true) {
            System.out.println("""
                    Choose roadmap:
                    1. Show All Roadmap
                    2. Show 1 Major Roadmap
                    """);
            System.out.print("Enter option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> displayAllRoadmap();
                case 2 -> displaySingleRoadmap();
                case 0 -> { return; }
                default -> {
                    System.out.println("Please enter option (1-2)");
                    chooseIdOrAllRoadmap();
                }
            }
        }
    }

    // ===============================
    // DISPLAY ALL
    // ===============================
    public void displayAllRoadmap() {
        roadmapService.displayAllRoadmap();
    }

    // ===============================
    // DISPLAY SINGLE ROADMAP
    // ===============================
    public void displaySingleRoadmap() {
        while (true) {
            System.out.println("""
                    Display one Roadmap by:
                    1. Roadmap ID
                    2. Major ID
                    0. Back
                    """);

            System.out.print("Please Enter Option: ");
            var option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> displayByRoadmapId();
                case 2 -> displayByMajorId();
                case 0 -> { return; }
                default -> System.out.println("Invalid option. Please enter 0-2.");
            }
        }
    }

    // ===============================
    // DISPLAY BY MAJOR
    // ===============================
    private void displayByMajorId() {
        System.out.print("Enter Major ID: ");
        var majorId = Integer.parseInt(scanner.nextLine());
        roadmapService.displaySingleRoadmap(majorId);
    }

    // ===============================
    // DISPLAY BY ROADMAP ID
    // ===============================
    private void displayByRoadmapId() {
        System.out.print("Enter Roadmap ID: ");
        var roadmapId = Integer.parseInt(scanner.nextLine());
        roadmapService.displayRoadmapById(roadmapId);
    }

    // ===============================
    // CREATE ROADMAP
    // ===============================
    public void create() {
        roadmapService.createRoadmap();
    }

    // ===============================
    // DELETE ROADMAP
    // ===============================
    public void delete() {
        System.out.print("Enter Roadmap ID to delete: ");
        var roadmapId = Integer.parseInt(scanner.nextLine());
        roadmapService.deleteRoadmap(roadmapId);
    }
}