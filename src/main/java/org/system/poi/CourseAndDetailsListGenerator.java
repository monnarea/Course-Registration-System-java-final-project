package org.system.poi;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CourseAndDetailsListGenerator {
    private CourseAndDetailsListGenerator(){

    }
    public static List<CourseAndDetails> get(){
        return Stream.of(
                new CourseAndDetails(1,"Introduction to IT",500.00,"Basic concepts of information technology",3,30,"2026-03-01","2026-06-01",1,"Room A1",1,1,"Monday to Friday","7:30-9:00","14:30-16:00","Null"),
                new CourseAndDetails(2,"Database Systems",700.00,"Learn relational database and SQL",4,25,"2026-03-01","2026-06-01",2,"Room B1",1,2,"Monday to Friday","7:30-9:00","13:00-14:30","Null"),
                new CourseAndDetails(6,"Full Stack Web Development",900.00,"Learn full stack development with React,Node.js,and Database",5,30,"2026-03-01","2026-06-01",3,"Room F1",1,3,"Monday to Friday","9:00-10:30","13:00-14:30","18:00-19:30"),
                new CourseAndDetails(3,"Network Security",800.00,"Fundamentals of network security",4,20,"2026-03-01","2026-06-01",3,"Room C1",2,1,"Monday to Friday","7:30-9:00","13:00-14:30","18:00-19:30"),
                new CourseAndDetails(4,"DevOps Fundamentals",750.00,"Learn the core DevOps principles, CI/CD, and automation tools",4,25,"2026-03-01","2026-06-01",1,"Room D1",3,1,"Monday to Friday","9:00-10:30","14:30-16:00","Null"),
                new CourseAndDetails(5,"Spring Boot Applications",800.00,"Build Modern Java application using Spring Boot",4,20,"2026-03-01","2026-06-01",2,"Room E1",4,1,"Monday to Friday","7:30-9:00","16:00-17:30","Null")

        ).collect(Collectors.toList());

    }

}