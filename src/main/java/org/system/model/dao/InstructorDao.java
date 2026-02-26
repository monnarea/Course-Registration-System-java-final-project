package org.system.model.dao;

import org.system.model.dto.response.InstructorResponseDto;

import java.util.List;

public interface InstructorDao {
    boolean createInstructor(InstructorResponseDto instructor);
    List<InstructorResponseDto> getAllInstructors();
    List<InstructorResponseDto> getInstructorById(int instructorId);
    boolean updateInstructor(InstructorResponseDto instructor);
    boolean deleteInstructor(int instructorId);
}
