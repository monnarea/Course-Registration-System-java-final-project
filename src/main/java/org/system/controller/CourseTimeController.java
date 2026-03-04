package org.system.controller;

import org.system.service.CourseService;
import org.system.service.CourseTimeService;

import java.sql.SQLException;
import java.util.Scanner;

public class CourseTimeController {
    private final CourseTimeService courseTimeService = new CourseTimeService();
    private final Scanner scanner = new Scanner(System.in);
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    public void displayAllCourseTime(){
        courseTimeService.displayAllCourseTime();
    }
    public void displayCourseTimeById(){
        try{
            System.out.print(yellow+"Enter course time Id: ");
            int id = scanner.nextInt();
            courseTimeService.displayCourseTimeById(id);
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input! Please input number");
        }

    }
    public void create() throws SQLException {
        courseTimeService.createCourseTime();
    }
    public void update(){
        try{
            System.out.print(yellow+"Enter course time Id to update: ");
            int id = scanner.nextInt();
            courseTimeService.updateCourseTime(id);
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input! Please input number");
        }

    }
    public void delete(){
        try{
            System.out.print(yellow+"Enter course time Id to delete: ");
            int id = scanner.nextInt();
            courseTimeService.deleteCourseTime(id);
        }catch (NumberFormatException e){
            System.out.println(red+"Invalid input! Please input number");
        }
    }
}
