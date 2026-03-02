package org.system.controller;

import org.system.service.CourseService;

import java.sql.SQLException;
import java.util.Scanner;

public class CourseController {
    private final CourseService courseService = new CourseService();
    private final Scanner scanner = new Scanner(System.in);

    public void displayAllCourse(){
        courseService.displayAllCourse();

    }
    public void displayCourseBy(){
        courseService.displayCourseById();

//        while (true)
//        {
//            System.out.println("""
//                Do you want to display one course by
//                1. Major Id
//                2. Course Id
//                0. Back
//                """);
//            System.out.print("Please Enter Option: ");
//            int option = scanner.nextInt();
//            switch (option){
//                case 1 -> {
//                    System.out.print("Enter Major Id: ");
//                    courseService.displaySingleCourseByMajorId(scanner.nextInt());
//                }
//                case 2 -> {
//                    System.out.print("Enter Course Id: ");
//                    courseService.displaySingleCourseByCourseId(scanner.nextInt());
//                }
//                case 0 -> { return;}
//                default -> System.out.println("Invalid option. Please enter 1-2.");
//            }
//        }

    }

    public void createCourse() throws SQLException {
        courseService.createCourse();
    }

    public void updateCourse() {
        System.out.print("Enter Course ID to update: ");
        int course_id = scanner.nextInt();
        courseService.updateCourse(course_id);
    }

    public void deleteCourse() {
        System.out.print("Enter Course ID to delete: ");
        int course_id = scanner.nextInt();
        courseService.deleteCourse(course_id);
    }
}
