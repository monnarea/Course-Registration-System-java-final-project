package org.system.model.dao;

import org.system.model.dto.response.InstructorResponseDto;

import java.sql.SQLException;

import java.util.List;

public interface InstructorDao {

    boolean createInstructor(InstructorResponseDto instructor);

    List<InstructorResponseDto> getAllInstructors();

    List<InstructorResponseDto> getInstructorById(int instructorId);

    InstructorResponseDto updateInstructor(int id,InstructorResponseDto instructor) throws SQLException;

    boolean deleteInstructor(int instructorId);

}
