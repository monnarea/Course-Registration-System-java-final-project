package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.response.CourseResponseDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CourseDaoImpl implements CourseDao{
    @Override
    public List<CourseResponseDto> getById(int course_id) throws SQLException {

        String sql = "SELECT * FROM course WHERE course_id = ?";

        List<CourseResponseDto> list = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
          PreparedStatement pstmt = connection.prepareStatement(sql)){
            // Set the ID parameter (replaces the ? in the SQL string)
            pstmt.setInt(1,course_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    CourseResponseDto course = new CourseResponseDto(
                            rs.getInt("course_id"),
                            rs.getString("course_name"),
                            rs.getDouble("price"),
                            rs.getInt("credit_score"),
                            rs.getInt("capacity"),
                            rs.getObject("start_date", LocalDate.class),
                            rs.getObject("end_date", LocalDate.class),
                            rs.getInt("instructor_id"),
                            rs.getString("room"),
                            rs.getTimestamp("created_at").toLocalDateTime().toLocalDate()
                    );
                    list.add(course);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error finding course with ID: " + course_id, e);
        }
        return list;
    }

    @Override
    public List<CourseResponseDto> getAll() throws SQLException {
        String sql = "SELECT * FROM course";
        List<CourseResponseDto> list = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Use 'while' to iterate through every row in the table
            while (rs.next()) {
                CourseResponseDto dto = new CourseResponseDto(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getDouble("price"),
                        // Note: If credit_score is DOUBLE in DB, rs.getInt will truncate it
                        (int) rs.getDouble("credit_score"),
                        rs.getInt("capacity"),
                        rs.getObject("start_date", LocalDate.class),
                        rs.getObject("end_date", LocalDate.class),
                        rs.getInt("instructor_id"),
                        rs.getString("room"),
                        // Converting SQL Timestamp to Java LocalDate
                        rs.getTimestamp("created_at") != null ?
                                rs.getTimestamp("created_at").toLocalDateTime().toLocalDate() : null
                );
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching all courses", e);
        }

        return list;
    }
}
