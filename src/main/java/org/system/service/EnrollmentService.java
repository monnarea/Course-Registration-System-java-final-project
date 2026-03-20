package org.system.service;

import org.system.exception.EnrollmentException;
import org.system.model.dao.EnrollmentDao;
import org.system.model.dao.EnrollmentDaoImpl;
import org.system.model.dto.request.EnrollmentRequestDto;

import java.util.List;

public class EnrollmentService {

    private final EnrollmentDao enrollmentDao;

    public EnrollmentService() {
        this.enrollmentDao = new EnrollmentDaoImpl();
    }

    // ── INSERT ────────────────────────────────────────────────────────────────
    public EnrollmentRequestDto insertEnrollment(EnrollmentRequestDto request) {
        if (request.getStudent_id() == null || request.getStudent_id() <= 0)
            throw new EnrollmentException("Invalid student ID.", 400);
        if (request.getCourse_id() == null || request.getCourse_id() <= 0)
            throw new EnrollmentException("Invalid course ID.", 400);

        boolean success = enrollmentDao.insertEnrollment(request);
        if (!success)
            throw new EnrollmentException("Failed to create enrollment.", 500);

        return request;
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────
    public List<EnrollmentRequestDto> getAllEnrollments() {
        List<EnrollmentRequestDto> list = enrollmentDao.getAllEnrollments();
        if (list == null || list.isEmpty())
            throw new EnrollmentException("No enrollments found.", 404);
        return list;
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────
    public EnrollmentRequestDto getEnrollmentById(int id) {
        EnrollmentRequestDto enrollment = enrollmentDao.getEnrollmentById(id);
        if (enrollment == null)
            throw new EnrollmentException("Enrollment not found with ID: " + id, 404);
        return enrollment;
    }

    // ── GET BY STUDENT ID ─────────────────────────────────────────────────────
    public List<EnrollmentRequestDto> getEnrollmentsByStudentId(long studentId) {
        if (studentId <= 0)
            throw new EnrollmentException("Invalid student ID.", 400);
        List<EnrollmentRequestDto> list = enrollmentDao.getEnrollmentsByStudentId(studentId);
        if (list == null || list.isEmpty())
            throw new EnrollmentException("No enrollments found for student ID: " + studentId, 404);
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public EnrollmentRequestDto updateEnrollment(EnrollmentRequestDto request) {
        EnrollmentRequestDto existing = enrollmentDao.getEnrollmentById(request.getEnrollment_id());
        if (existing == null)
            throw new EnrollmentException("Enrollment not found with ID: " + request.getEnrollment_id(), 404);

        boolean success = enrollmentDao.updateEnrollment(request);
        if (!success)
            throw new EnrollmentException("Failed to update enrollment ID: " + request.getEnrollment_id(), 500);

        return request;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public void deleteEnrollment(int id) {
        EnrollmentRequestDto existing = enrollmentDao.getEnrollmentById(id);
        if (existing == null)
            throw new EnrollmentException("Enrollment not found with ID: " + id, 404);

        boolean success = enrollmentDao.deleteEnrollment(id);
        if (!success)
            throw new EnrollmentException("Failed to delete enrollment ID: " + id, 500);
    }
}