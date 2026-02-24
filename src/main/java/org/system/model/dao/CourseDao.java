package org.system.model.dao;

import org.system.model.dto.response.CourseResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface CourseDao {

    List<CourseResponseDto> getByMajorId(int major_id) throws SQLException;
    List<CourseResponseDto> getById(int course_id) throws SQLException;

    List<CourseResponseDto> getAll() throws SQLException;

}
