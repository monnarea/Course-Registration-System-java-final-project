package org.system.service;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.StudentRequestDto;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.*;

public class StudentService {

    private final Scanner sc = new Scanner(System.in);

    // 👉 INSERT INTO DATABASE
    public void createStudent() {
        StudentRequestDto student = new StudentRequestDto();

        System.out.print("Enter Name: ");
        student.setStudent_name(sc.nextLine());

        System.out.print("Enter Gender: ");
        student.setGender(sc.nextLine());

        System.out.print("Enter DateOfBirth: ");
        student.setDate_of_birth(sc.nextLine());

        System.out.print("Enter Email: ");
        student.setEmail(sc.nextLine());

        System.out.print("Enter Phone Number: ");
        student.setPhone_number(Integer.valueOf(sc.nextLine()));  // ✅ Changed to String (phone numbers shouldn't be int)

        System.out.print("Enter Semester: ");
        student.setSemester(sc.nextLine());

        System.out.print("Enter Year: ");
        student.setYear(Integer.parseInt(sc.nextLine()));

        System.out.print("Enter University: ");
        student.setUniversity(sc.nextLine());

        // 🔥 SAVE TO DATABASE
        String sql = "INSERT INTO student " +
                "(student_name, gender, date_of_birth, email, phone_number, semester, year, university) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, student.getStudent_name());
            ps.setString(2, student.getGender());
            ps.setString(3, student.getDate_of_birth());
            ps.setString(4, student.getEmail());
            ps.setInt(5, student.getPhone_number());
            ps.setString(6, student.getSemester());
            ps.setInt(7, student.getYear());
            ps.setString(8, student.getSemester());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Student created successfully!");
            } else {
                System.out.println("⚠️ Insert failed. No rows affected.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter the correct data type.");
        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<StudentRequestDto> getAllStudents() {
        return List.of();
    }

    public boolean getStudentById(int id) {
        return false;
    }

    public void updateStudent(int i) {
    }

    public void deleteStudent(int i) {
    }
}

