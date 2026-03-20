package org.system.controller;

import org.system.service.StudentService;
import java.util.Scanner;

public class StudentController {

    private final StudentService service;
    private final Scanner scanner;

    public StudentController() {
        this.service = new StudentService();
        this.scanner = new Scanner(System.in);
    }

    // ── 1. DISPLAY ALL ────────────────────────────────────────────────────────
    public void displayAllStudents() {
        service.getAllStudents();
    }

    // ── 2. DISPLAY BY ID ──────────────────────────────────────────────────────
    public void displayStudentById() {
        System.out.print("Enter Student ID: ");
        int id = readInt();
        service.getStudentById(id);
    }

    // ── 3. CREATE ─────────────────────────────────────────────────────────────
    public void createStudent() {
        service.createStudent();
    }

    // ── 4. UPDATE ─────────────────────────────────────────────────────────────
    public void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        int id = readInt();
        service.updateStudent(id);
    }

    // ── 5. DELETE ─────────────────────────────────────────────────────────────
    public void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        int id = readInt();
        service.deleteStudent(id);
    }

    // ── HELPER: safe int read ─────────────────────────────────────────────────
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}