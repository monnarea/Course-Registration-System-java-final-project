package org.system.controller;

import org.system.model.dao.CourseDaoImpl;
import org.system.service.CourseService;
import org.system.service.RoadmapService;

import java.util.Scanner;

public class CourseController {
    private final CourseService courseService = new CourseService();
    private final Scanner scanner = new Scanner(System.in);

    private void displayAllCourse(){
        courseService.displayAllCourse();

    }
    public void displayCourseBy(){
        System.out.println("""
                Do you want to display one course by
                1. Course Id
                2. Major Id
                """);
        System.out.print("Please Enter Option: ");
        int option = scanner.nextInt();
        if (option == 1){
            System.out.print("Enter Major Id: ");
            courseService.displaySingleCourse(scanner.nextInt());
        } else if (option==2) {

        }else {
            System.out.println("Enter option( 1-2 )");
            displayCourseBy();
        }
    }
}
