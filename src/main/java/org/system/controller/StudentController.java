package org.system.controller;

import org.system.model.dao.StudentDao;
import org.system.model.dto.response.StudentResponseDto;

import java.util.List;

public class StudentController {

    private final StudentDao studentDao;

    public StudentController(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    // Create new student
    public void createStudent(StudentResponseDto student) {
        if (student == null) {
            System.out.println("Student cannot be null.");
            return;
        }
        studentDao.insert(student);
        System.out.println("Student created successfully.");
    }

    // Get all students
    public List<StudentResponseDto> getAllStudents() {
        List<StudentResponseDto> students = studentDao.findAll();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        }
        return students;
    }

    // Get student by ID
    public StudentResponseDto getStudentById(Integer id) {
        if (id == null || id <= 0) {
            System.out.println("Invalid student ID.");
            return null;
        }
        StudentResponseDto student = studentDao.findById(id);
        if (student == null) {
            System.out.println("Student with ID " + id + " not found.");
        }
        return student;
    }

    // Update student
    public void updateStudent(Integer id, StudentResponseDto student) {
        if (id == null || id <= 0) {
            System.out.println("Invalid student ID.");
            return;
        }
        StudentResponseDto existing = studentDao.findById(id);
        if (existing == null) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }

    }

    // Delete student by ID
    public void deleteStudent(Integer id) {
        if (id == null || id <= 0) {
            System.out.println("Invalid student ID.");
            return;
        }
        StudentResponseDto existing = studentDao.findById(id);
        if (existing == null) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }
        studentDao.delete(id);
        System.out.println("Student with ID " + id + " deleted successfully.");
    }
}