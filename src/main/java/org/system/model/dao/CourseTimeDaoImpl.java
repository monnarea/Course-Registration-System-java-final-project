package org.system.model.dao;

import org.system.config.DatabaseConfig;

import org.system.model.dto.response.CourseTimeResponseDto;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;

public class CourseTimeDaoImpl implements CourseTimeDao{
    @Override
    public List<CourseTimeResponseDto> getAll() throws SQLException {
        String sql = """
        select * from course_time 
    """;
        List<CourseTimeResponseDto> list = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Use 'while' to iterate through every row in the table
            while (rs.next()) {
                CourseTimeResponseDto dto = new CourseTimeResponseDto(
                        rs.getInt("time_id"),
                        rs.getInt("course_id"),
                        rs.getString("day_of_week"),
                        rs.getString("morning"),
                        rs.getString("afternoon"),
                        rs.getString("evening")
                );
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching all courses", e);
        }

        return list;
    }

    @Override
    public List<CourseTimeResponseDto> getById(int id) throws SQLException {
        String sql = "select * from course_time where time_id = ? ";
        List<CourseTimeResponseDto> list = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {

                // Use 'while' to iterate through every row in the table
                if (rs.next()) {
                    CourseTimeResponseDto dto = new CourseTimeResponseDto(
                            rs.getInt("time_id"),
                            rs.getInt("course_id"),
                            rs.getString("day_of_week"),
                            rs.getString("morning"),
                            rs.getString("afternoon"),
                            rs.getString("evening")
                    );
                    list.add(dto);
            }
        }
             } catch (SQLException e) {
            throw new SQLException("Error fetching all courses", e);
        }

        return list;
    }

    @Override
    public CourseTimeResponseDto create(CourseTimeResponseDto courseTimeResponseDto) throws SQLException {

        String sql = """
            INSERT INTO course_time
                (time_id, course_id, day_of_week, morning,
                 afternoon, evening)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, courseTimeResponseDto.getCourse_id());
            pstmt.setString(2,courseTimeResponseDto.getDay_of_week());
            pstmt.setString(3,courseTimeResponseDto.getMorning());
            pstmt.setString(4,courseTimeResponseDto.getAfternoon());
            pstmt.setString(5,courseTimeResponseDto.getEvening());


            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating course failed, no rows affected.");
            }

            // Retrieve the auto-generated course_id
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    // Fetch the full record (including joined major_name) and return it
                    List<CourseTimeResponseDto> result = getById(newId);
                    if (!result.isEmpty()) return result.get(0);
                }
            }
            throw new SQLException("Creating course Time failed, no ID obtained.");

        } catch (SQLException e) {
            throw new SQLException("Error creating course Time: " + e.getMessage(), e);
        }
    }

    @Override
    public CourseTimeResponseDto update(int id,CourseTimeResponseDto courseTimeResponseDto) throws SQLException {
        String sql = """
            UPDATE course SET
                time_id = ?,
                course_id = ? ,
                day_of_week = ?,
                morning = ?,
                afternoon = ?,
                evening = ?
            WHERE time_id = ?
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseTimeResponseDto.getCourse_id());
            pstmt.setString(2, courseTimeResponseDto.getDay_of_week());
            pstmt.setString(3,    courseTimeResponseDto.getMorning());
            pstmt.setString(4,    courseTimeResponseDto.getAfternoon());
            pstmt.setString(5,    courseTimeResponseDto.getEvening());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed — no course Time found with ID: " + id);
            }

            // Return the updated record
            List<CourseTimeResponseDto> result = getById(id);
            if (!result.isEmpty()) return result.get(0);
            throw new SQLException("Could not retrieve updated course Time with ID: " + id);

        } catch (SQLException e) {
            throw new SQLException("Error updating course Time with ID: " + id, e);
        }
    }

    @Override
    public boolean delete(int courseTime_id) throws SQLException {
        String sql = "DELETE FROM course_time WHERE time_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseTime_id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // true = deleted, false = ID not found

        } catch (SQLException e) {
            throw new SQLException("Error deleting course Time with ID: " + courseTime_id, e);
        }
    }
}

