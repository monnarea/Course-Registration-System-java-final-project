package org.system.model.dao;

import org.system.model.dto.response.CourseTimeResponseDto;

import java.sql.SQLException;

import java.util.List;

public interface CourseTimeDao {

    List<CourseTimeResponseDto> getAll() throws SQLException;

    List<CourseTimeResponseDto> getById(int id) throws SQLException;

    CourseTimeResponseDto create(CourseTimeResponseDto courseTimeResponseDto) throws SQLException;

    CourseTimeResponseDto update(int id ,CourseTimeResponseDto courseTimeResponseDto) throws SQLException;

    boolean delete(int courseTime_id) throws SQLException;

}
