package org.system;
import org.system.config.DatabaseConfig;
import org.system.model.dao.CourseDao;
import org.system.model.dao.CourseDaoImpl;
import org.system.model.dao.RoadmapDao;
import org.system.model.dao.RoadmapDaoImpl;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.RoadmapResponseDto;
import org.system.view.MainMenu;

import java.awt.image.ByteLookupTable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.system.view.View.*;

public class Main {
    public static void main(String[] args) {
        new MainMenu().start();
//        Test database
        Connection con = DatabaseConfig.getConnection();

        if (con != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Connection failed.");
        }

////        Test course
//        CourseDaoImpl courseDao = new CourseDaoImpl();
//
//        // ✅ Test getAll()
//        System.out.println("===== Testing getAll() =====");
//        try {
//            List<CourseResponseDto> allCourses = courseDao.getAll();
//
//            if (allCourses.isEmpty()) {
//                System.out.println("No courses found in database.");
//            } else {
//                System.out.println("Total courses found: " + allCourses.size());
//
//                printCourseTable(allCourses);
//
//            }
//        } catch (SQLException e) {
//            System.err.println("getAll() failed: " + e.getMessage());
//        }
//
//        System.out.println();
//
////
////
////        System.out.println("===== Testing getById(1) =====");
////        try {
////            List<CourseResponseDto> result = courseDao.getById(1); // change 1 to any ID
////            if (result.isEmpty()) {
////                System.out.println("No course found.");
////            } else {
////                printCorseTable(result); // reuse same printTable method
////            }
////        } catch (SQLException e) {
////            System.err.println("getById() failed: " + e.getMessage());
////        }


//        RoadmapDao roadmapDao = new RoadmapDaoImpl();
//        try {
//            List<RoadmapResponseDto> allroadmap = roadmapDao.getAll();
//        if (allroadmap.isEmpty()) {
//            System.out.println("No courses found in database.");
//        } else {
//            System.out.println("Total courses found: " +allroadmap.size());
//
//
//            printRoadmapTable(allroadmap);
//
//        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        RoadmapDao roadmapDaoByid = new RoadmapDaoImpl();
//        try {
//            List<RoadmapResponseDto> allroadmap1 = roadmapDaoByid.getById(2);
//            if (allroadmap1.isEmpty()) {
//                System.out.println("No courses found in database.");
//            } else {
//                System.out.println("Total courses found: " +allroadmap1.size());
//
//
//                printSingleRoadmapTable(allroadmap1);
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
