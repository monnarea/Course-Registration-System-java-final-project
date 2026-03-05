package org.system.model.dao;

import org.system.model.dto.response.StudentResponseDto;
import java.util.List;

public interface StudentDao {

    // Insert new student
    void insert(StudentResponseDto student);

    // Get all students
    List<StudentResponseDto> findAll();

    // Get student by ID
    StudentResponseDto findById(Integer id);

    // Update student
    void update(StudentResponseDto student);

    // Delete student by ID
    void delete(Integer id);
}