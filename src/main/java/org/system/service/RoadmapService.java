package org.system.service;

import org.system.model.dao.RoadmapDao;
import org.system.model.dao.RoadmapDaoImpl;
import org.system.model.dto.request.RoadmapRequestDto;
import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printRoadmapTable;
import static org.system.view.View.printSingleRoadmapTable;

public class RoadmapService {
    private final Scanner scanner = new Scanner(System.in);
    private final RoadmapDao roadmapDao = new RoadmapDaoImpl();

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

    public void createRoadmap() {
        System.out.println("\n========== Create New Roadmap ==========");
        int courseId = 0;
        int subId = 0;
        int majorId =0 ;
            try {
                System.out.print("Course ID : ");
                courseId = Integer.parseInt(scanner.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Invalid input ! Please input number");

            }
            try {
                System.out.print("Subject ID: ");
                subId = Integer.parseInt(scanner.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Invalid input ! Please input number");

            }
            try {
                System.out.print("Major ID: ");
                majorId = Integer.parseInt(scanner.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Invalid input ! Please input number");

            }
            try {
                RoadmapRequestDto request = RoadmapRequestDto.builder()
                       .courseId(courseId)
                       .subId(subId)
                       .majorId(majorId)
                       .build();

               RoadmapResponseDto created = roadmapDao.create(request);

               System.out.println("\n✔ Roadmap created successfully!");
               printSingleRoadmapTable(List.of(created));
           } catch (SQLException e) {
            System.out.println("Create roadmap failed: " + e.getMessage());
        }

        System.out.println();
    }


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