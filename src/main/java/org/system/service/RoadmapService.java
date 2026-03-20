
package org.system.service;

import org.system.model.dao.RoadmapDao;
import org.system.model.dao.RoadmapDaoImpl;
import org.system.model.dto.request.RoadmapRequestDto;
import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.*;
import org.system.util.Pagination;

public class RoadmapService {
    private final Scanner scanner = new Scanner(System.in);
    private final RoadmapDao roadmapDao = new RoadmapDaoImpl();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    public void displayAllRoadmap() {
        try {
            List<RoadmapResponseDto> list = roadmapDao.getAll();

            if (list.isEmpty()) {
                System.out.println(roadmapDao+"No roadmap data found.");
                return;
            }

            System.out.println(cyan+"Total roadmap records: " + list.size());
            printRoadmapTablePaginated(list);

        } catch (SQLException e) {
            System.out.println(red+"Error retrieving roadmap data.");
            e.printStackTrace();
        }
    }

    public void displayRoadmapById(int id) {
        try {
            List<RoadmapResponseDto> list = roadmapDao.getById(id);

            if (list.isEmpty()) {
                System.out.println(red+"No roadmap data found.");
                return;
            }

            System.out.println(cyan+"Total roadmap records: " + list.size());
            printRoadmapTable(list);

        } catch (SQLException e) {
            System.out.println(red+"Error retrieving roadmap data.");
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
        System.out.println(cyan+"\n========== Create New Roadmap ==========");
        int courseId = 0;
        int subId = 0;
        int majorId =0 ;
        try {
            System.out.print(yellow+"Course ID : ");
            courseId = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input ! Please input number");

        }
        try {
            System.out.print(yellow+"Subject ID: ");
            subId = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input ! Please input number");

        }
        try {
            System.out.print(yellow+"Major ID: ");
            majorId = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input ! Please input number");

        }
        try {
            RoadmapRequestDto request = RoadmapRequestDto.builder()
                    .courseId(courseId)
                    .subId(subId)
                    .majorId(majorId)
                    .build();

            RoadmapResponseDto created = roadmapDao.create(request);

            System.out.println(green+"\n✔ Roadmap created successfully!");
            printSingleRoadmapTable(List.of(created));
        } catch (SQLException e) {
            System.out.println(red+"Create roadmap failed: " + e.getMessage());
        }

        System.out.println();
    }


    public void deleteRoadmap(int roadmapId) {

        System.out.println(cyan+"\n========== Delete Roadmap ==========");
        try {
            boolean deleted = roadmapDao.delete(roadmapId);

            if (deleted) {
                System.out.println(green+"Roadmap deleted successfully!");
            } else {
                System.out.println(red+"No roadmap found with ID: " + roadmapId);
            }

        } catch (SQLException e) {
            System.out.println(red+"Error deleting roadmap.");
            e.printStackTrace();
        }
    }
    public void updateRoadmap(int id) {
        boolean validInput;
        System.out.println(cyan + "\n========== Update Roadmap (ID: " + id + ") ==========");

        // ── Fetch current data ───────────────────────────────────
        RoadmapResponseDto current;
        try {
            List<RoadmapResponseDto> existing = roadmapDao.getById(id);
            if (existing.isEmpty()) {
                System.out.println(yellow + "No roadmap found with ID: " + id);
                return;
            }
            current = existing.get(0);
            System.out.println(yellow + "Current details:");
            printSingleRoadmapTable(existing);
        } catch (SQLException e) {
            System.err.println(red + "Could not fetch roadmap: " + e.getMessage());
            return;
        }

        System.out.println(cyan + "\nPress Enter to keep the current value, or type a new one:");

        // ── Course ID ────────────────────────────────────────────
        int courseId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Course ID  [" + current.getCourse_id() + "]: ");
                String input = scanner.nextLine().trim();
                courseId = input.isEmpty() ? current.getCourse_id() : Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a valid number.");
            }
        }

        // ── Subject ID ───────────────────────────────────────────
        int subId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Subject ID [" + current.getSub_id() + "]: ");
                String input = scanner.nextLine().trim();
                subId = input.isEmpty() ? current.getSub_id() : Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a valid number.");
            }
        }

        // ── Major ID ─────────────────────────────────────────────
        int majorId = 0;
        validInput = false;
        while (!validInput) {
            try {
                System.out.print(yellow + "Major ID   [" + current.getMajor_id() + "]: ");
                String input = scanner.nextLine().trim();
                majorId = input.isEmpty() ? current.getMajor_id() : Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid! Please enter a valid number.");
            }
        }

        // ── Build & Send ─────────────────────────────────────────
        try {
            RoadmapRequestDto request = RoadmapRequestDto.builder()
                    .courseId(courseId)
                    .subId(subId)
                    .majorId(majorId)
                    .build();

            RoadmapResponseDto updated = roadmapDao.update(id, request);
            System.out.println(green + "\n✔ Roadmap updated successfully!");
            printSingleRoadmapTable(List.of(updated));
        } catch (SQLException e) {
            System.err.println(red + "Update failed: " + e.getMessage());
        }
    }
}
