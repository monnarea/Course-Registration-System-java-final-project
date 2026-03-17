package org.system.exception;

public class EnrollmentException extends RuntimeException {

    private final int statusCode;

    public EnrollmentException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    // ── Common static factory methods ─────────────────────────────────────────
    public static EnrollmentException notFound(int id) {
        return new EnrollmentException("Enrollment not found with ID: " + id, 404);
    }

    public static EnrollmentException insertFailed() {
        return new EnrollmentException("Failed to insert enrollment.", 500);
    }

    public static EnrollmentException updateFailed(int id) {
        return new EnrollmentException("Failed to update enrollment with ID: " + id, 500);
    }

    public static EnrollmentException deleteFailed(int id) {
        return new EnrollmentException("Failed to delete enrollment with ID: " + id, 500);
    }

    public static EnrollmentException invalidStudent() {
        return new EnrollmentException("Student ID is invalid or does not exist.", 400);
    }

    public static EnrollmentException invalidCourse() {
        return new EnrollmentException("Course ID is invalid or does not exist.", 400);
    }
}