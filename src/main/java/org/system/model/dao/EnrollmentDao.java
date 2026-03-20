package org.system.model.dao;

import org.system.model.dto.request.EnrollmentRequestDto;
import java.util.List;

public interface EnrollmentDao {
    boolean insertEnrollment(EnrollmentRequestDto e);
    EnrollmentRequestDto getEnrollmentById(int id);
    List<EnrollmentRequestDto> getEnrollmentsByStudentId(long studentId);
    List<EnrollmentRequestDto> getAllEnrollments();
    boolean updateEnrollment(EnrollmentRequestDto e);
    boolean deleteEnrollment(int id);
}