package org.system.service;

import org.system.exception.EnrollmentException;
import org.system.model.dao.EnrollmentDao;
import org.system.model.dao.EnrollmentDaoImpl;
import org.system.model.dto.request.EnrollmentRequestDto;

import java.time.LocalDate;
import java.util.List;

public class EnrollmentService {

    private final EnrollmentDao enrollmentDao;
    private static final String TELEGRAM_BOT_LINK = "https://t.me/your_bot_name"; // 🔁 Replace with your bot link

    public EnrollmentService() {
        this.enrollmentDao = new EnrollmentDaoImpl();
    }

    // ── ENROLL STUDENT ────────────────────────────────────────────────────────
    public EnrollmentRequestDto enrollStudent(EnrollmentRequestDto request) {

        // Validate inputs
        if (request.getStudent_id() == null || request.getStudent_id() <= 0) {
            throw EnrollmentException.invalidStudent();
        }
        if (request.getCourse_id() == null || request.getCourse_id() <= 0) {
            throw EnrollmentException.invalidCourse();
        }

        // Auto-set enroll date if not provided
        if (request.getEnrollment_date() == null) {
            request.setEnrollment_date(LocalDate.now());
        }

        // Default status


        boolean success = enrollmentDao.insertEnrollment(request);
        if (!success) {
            throw EnrollmentException.insertFailed();
        }

        return request;
    }

    // ── GET ALL ENROLLMENTS ───────────────────────────────────────────────────
    public List<EnrollmentRequestDto> getAllEnrollments() {
        List<EnrollmentRequestDto> list = enrollmentDao.getAllEnrollments();
        if (list == null || list.isEmpty()) {
            throw new EnrollmentException("No enrollments found.", 404);
        }
        return list;
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────
    public EnrollmentRequestDto getEnrollmentById(int id) {
        EnrollmentRequestDto enrollment = enrollmentDao.getEnrollmentById(id);
        if (enrollment == null) {
            throw EnrollmentException.notFound(id);
        }
        return enrollment;
    }

    // ── GET BY STUDENT ID ─────────────────────────────────────────────────────
    public List<EnrollmentRequestDto> getEnrollmentsByStudentId(int studentId) {
        if (studentId <= 0) {
            throw EnrollmentException.invalidStudent();
        }
        List<EnrollmentRequestDto> list = enrollmentDao.getEnrollmentsByStudentId(studentId);
        if (list == null || list.isEmpty()) {
            throw new EnrollmentException("No enrollments found for student ID: " + studentId, 404);
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public EnrollmentRequestDto updateEnrollment(EnrollmentRequestDto request) {
        // Check exists first
        EnrollmentRequestDto existing = enrollmentDao.getEnrollmentById(request.getEnrollment_id());
        if (existing == null) {
            throw EnrollmentException.notFound(request.getEnrollment_id());
        }

        boolean success = enrollmentDao.updateEnrollment(request);
        if (!success) {
            throw EnrollmentException.updateFailed(request.getEnrollment_id());
        }
        return request;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public void deleteEnrollment(int id) {
        EnrollmentRequestDto existing = enrollmentDao.getEnrollmentById(id);
        if (existing == null) {
            throw EnrollmentException.notFound(id);
        }

        boolean success = enrollmentDao.deleteEnrollment(id);
        if (!success) {
            throw EnrollmentException.deleteFailed(id);
        }
    }

    // ── GET TELEGRAM BOT LINK ─────────────────────────────────────────────────
    public String getTelegramBotLink() {
        return TELEGRAM_BOT_LINK;
    }
}