package org.system.controller;


import org.system.model.dto.request.StudentRequestDto;

import org.system.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class StudentController {

     static void main(String[] args) {

        StudentService service = new StudentService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Find Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    service.createStudent();
                    break;

                case 2:
                    List<StudentRequestDto> students = service.getAllStudents();
                    students.forEach(System.out::println);
                    break;

                case 3:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    System.out.println(service.getStudentById(id));
                    break;

                case 4:
                    System.out.print("Enter ID to update: ");
                    service.updateStudent(sc.nextInt());
                    break;

                case 5:
                    System.out.print("Enter ID to delete: ");
                    service.deleteStudent(sc.nextInt());
                    break;

                case 0:
                    System.out.println("Goodbye 👋");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}