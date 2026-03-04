package org.system.service;

import org.system.model.dao.InstructorDao;
import org.system.model.dao.InstructorDaoImpl;
import org.system.model.dto.response.InstructorResponseDto;

import java.util.List;

import static org.system.view.View.printInstructorTable;

public class InstructorService {

    private final InstructorDao instructorDao = new InstructorDaoImpl();

    // =========================================================
    //  CREATE
    // =========================================================
    public boolean createInstructor(InstructorResponseDto instructor) {
        if (instructor == null) {
            System.err.println("[SERVICE - CREATE] Instructor cannot be null.");
            return false;
        }
        if (instructor.getInstructorName() == null || instructor.getInstructorName().trim().isEmpty()) {
            System.err.println("[SERVICE - CREATE] Instructor name cannot be empty.");
            return false;
        }
        if (instructor.getEmail() == null || instructor.getEmail().trim().isEmpty()) {
            System.err.println("[SERVICE - CREATE] Email cannot be empty.");
            return false;
        }
        if (instructor.getAge() <= 0) {
            System.err.println("[SERVICE - CREATE] Age must be greater than 0.");
            return false;
        }
        return instructorDao.createInstructor(instructor);
    }

    // =========================================================
    //  READ ALL
    // =========================================================
    public void getAllInstructors() {
        List<InstructorResponseDto> list = instructorDao.getAllInstructors();
        if (list.isEmpty()) {
            System.err.println("[SERVICE - READ ALL] No instructors found.");
            return;
        }
        printInstructorTable(list);
    }

    // =========================================================
    //  READ BY ID
    // =========================================================
    public void getInstructorById(int instructorId) {
        if (instructorId <= 0) {
            System.err.println("[SERVICE - READ BY ID] Invalid instructor_id: " + instructorId);
            return;
        }
        List<InstructorResponseDto> list = instructorDao.getInstructorById(instructorId);
        if (list.isEmpty()) {
            System.err.println("[SERVICE - READ BY ID] No instructor found with id = " + instructorId);
            return;
        }
        printInstructorTable(list);
    }

    // =========================================================
    //  UPDATE
    // =========================================================
    public boolean updateInstructor(InstructorResponseDto instructor) {
        if (instructor == null || instructor.getInstructorId() <= 0) {
            System.err.println("[SERVICE - UPDATE] Invalid instructor.");
            return false;
        }
        return instructorDao.updateInstructor(instructor);
    }

    // =========================================================
    //  DELETE
    // =========================================================
    public boolean deleteInstructor(int instructorId) {
        if (instructorId <= 0) {
            System.err.println("[SERVICE - DELETE] Invalid instructor_id: " + instructorId);
            return false;
        }
        List<InstructorResponseDto> existing = instructorDao.getInstructorById(instructorId);
        if (existing.isEmpty()) {
            System.err.println("[SERVICE - DELETE] Instructor not found with id: " + instructorId);
            return false;
        }
        return instructorDao.deleteInstructor(instructorId);
    }
}