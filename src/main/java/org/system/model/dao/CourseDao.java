package org.system.model.dao;

import org.system.model.dto.request.CourseRequestDto;

import org.system.model.dto.response.CourseResponseDto;

import java.sql.SQLException;

import java.util.List;

public interface CourseDao {

    List<CourseResponseDto> getByMajorId(int major_id) throws SQLException;

    List<CourseResponseDto> getById(int course_id) throws SQLException;

    List<CourseResponseDto> getAll() throws SQLException;
    // CREATE
    CourseResponseDto create(CourseRequestDto request) throws SQLException;

    // UPDATE
    CourseResponseDto update(int course_id, CourseRequestDto request) throws SQLException;

    // DELETE
    boolean delete(int course_id) throws SQLException;

}
