package org.system.service;

import org.system.model.dao.RoadmapDao;
import org.system.model.dao.RoadmapDaoImpl;
import org.system.model.dto.request.RoadmapRequestDto;
import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printRoadmapTable;
import static org.system.view.View.printSingleRoadmapTable;

public class RoadmapService {
    private final Scanner scanner = new Scanner(System.in);
    private final RoadmapDao roadmapDao = new RoadmapDaoImpl();

    // ===============================
    // DISPLAY ALL
    // ===============================
    public void displayAllRoadmap() {
        try {
            List<RoadmapResponseDto> list = roadmapDao.getAll();

            if (list.isEmpty()) {
                System.out.println("No roadmap data found.");
                return;
            }

            System.out.println("Total roadmap records: " + list.size());
            printRoadmapTable(list);

        } catch (SQLException e) {
            System.out.println("Error retrieving roadmap data.");
            e.printStackTrace();
        }
    }

    public void displayRoadmapById(int id) {
        try {
            List<RoadmapResponseDto> list = roadmapDao.getById(id);

            if (list.isEmpty()) {
                System.out.println("No roadmap data found.");
                return;
            }

            System.out.println("Total roadmap records: " + list.size());
            printRoadmapTable(list);

        } catch (SQLException e) {
            System.out.println("Error retrieving roadmap data.");
            e.printStackTrace();
        }
    }

    // ===============================
    // DISPLAY BY MAJOR
    // ===============================
    public void displaySingleRoadmap(int majorId) {
        try {
            List<RoadmapResponseDto> list = roadmapDao.getByMajorId(majorId);

            if (list.isEmpty()) {
                System.out.println("No roadmap found for major ID: " + majorId);
                return;
            }

            System.out.println("Total roadmap records: " + list.size());
            printSingleRoadmapTable(list);

        } catch (SQLException e) {
            System.out.println("Error retrieving roadmap by major.");
            e.printStackTrace();
        }
    }

    // ===============================
    // CREATE ROADMAP
    // ===============================
    public void createRoadmap() {
        System.out.println("\n========== Create New Roadmap ==========");

        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Course ID : ");
            int courseId = Integer.parseInt(scanner.nextLine());

            System.out.print("Subject ID: ");
            int subId = Integer.parseInt(scanner.nextLine());

            RoadmapRequestDto request = RoadmapRequestDto.builder()
                    .courseId(courseId)
                    .subId(subId)
                    .build();

            RoadmapResponseDto created = roadmapDao.create(request);

            System.out.println("\n✔ Roadmap created successfully!");
            printSingleRoadmapTable(List.of(created));

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter numeric values.");
        } catch (SQLException e) {
            System.err.println("Create roadmap failed: " + e.getMessage());
        }

        System.out.println();
    }

    // ===============================
    // DELETE ROADMAP
    // ===============================
    public void deleteRoadmap(int roadmapId) {

        System.out.println("\n========== Delete Roadmap ==========");
        try {
            boolean deleted = roadmapDao.delete(roadmapId);

            if (deleted) {
                System.out.println("Roadmap deleted successfully!");
            } else {
                System.out.println("No roadmap found with ID: " + roadmapId);
            }

        } catch (SQLException e) {
            System.out.println("Error deleting roadmap.");
            e.printStackTrace();
        }
    }
}