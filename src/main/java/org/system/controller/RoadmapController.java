package org.system.controller;

import org.system.service.RoadmapService;

import java.util.Scanner;

public class RoadmapController {
    private final RoadmapService roadmapService = new RoadmapService();
    private final Scanner scanner = new Scanner(System.in);

    public void chooseIdOrAllRoadmap(){
        System.out.println("""
                choose roadmap
                1. Show All Roadmap
                2. Show 1 major Roadmap
                """);
        System.out.print("Enter option: ");
        int option = scanner.nextInt();
        if (option == 1) {
            displayAllRoadmap();
        }else {
            displaySingleRoadmap();
        }
    }
    public void displayAllRoadmap() {
        roadmapService.diplayAllRoadmap();
    }

    public void displaySingleRoadmap() {
        System.out.print("Enter Major ID: ");
        int majorId = scanner.nextInt();
        roadmapService.diplaySingleRoadmap(majorId);
    }
}