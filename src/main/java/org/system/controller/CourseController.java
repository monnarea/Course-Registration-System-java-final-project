package org.system.controller;

import org.system.service.CourseService;

import java.sql.SQLException;
import java.util.Scanner;

public class CourseController {
    private final CourseService courseService = new CourseService();
    private final Scanner scanner = new Scanner(System.in);
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

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
        try {
            System.out.print(yellow+"Enter Course ID to update: ");
            int course_id = scanner.nextInt();
            courseService.updateCourse(course_id);
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input ! Please input number");
        }
    }

    public void deleteCourse() {
        try{
        System.out.print(yellow+"Enter Course ID to delete: ");
        int course_id = scanner.nextInt();
        courseService.deleteCourse(course_id);}
        catch (NumberFormatException e){
            System.out.println(red+"Invalid input ! Please input number");
        }
    }
}
