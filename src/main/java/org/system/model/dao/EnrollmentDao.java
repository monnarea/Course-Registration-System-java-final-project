
package org.system.model.dao;

import org.system.model.dto.request.EnrollmentRequestDto;

import java.util.List;

public interface EnrollmentDao {

    boolean insertEnrollment(EnrollmentRequestDto enrollment);

    EnrollmentRequestDto getEnrollmentById(int id);

    List<EnrollmentRequestDto> getEnrollmentsByStudentId(int studentId);

    List<EnrollmentRequestDto> getAllEnrollments();

    boolean updateEnrollment(EnrollmentRequestDto enrollment);

    boolean deleteEnrollment(int id);
}
