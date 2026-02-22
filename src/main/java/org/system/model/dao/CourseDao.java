package org.system.model.dao;

import org.system.model.dto.response.CourseResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface CourseDao {

    List<CourseResponseDto> getById(int courseId) throws SQLException;

    List<CourseResponseDto> getAll() throws SQLException;

}
