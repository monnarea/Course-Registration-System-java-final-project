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
    public List<CourseResponseDto> getByMajorId(int major_id) throws SQLException {

        String sql = """
        SELECT 
            c.course_id,
            c.course_name,
            c.price,
            c.credit_score,
            c.capacity,
            c.start_date,
            c.end_date,
            c.instructor_id,
            c.room,
            c.created_at,
            c.major_id,
            c.level,
            m.major_name
        FROM course c
        JOIN major m ON c.major_id = m.major_id
        WHERE c.major_id = ?
        ORDER BY c.level ASC
    """;

        List<CourseResponseDto> list = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
          PreparedStatement pstmt = connection.prepareStatement(sql)){
            // Set the ID parameter (replaces the ? in the SQL string)
            pstmt.setInt(1,major_id);

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
                            rs.getTimestamp("created_at").toLocalDateTime().toLocalDate(),
                            rs.getInt("major_id"),
                            rs.getString("major_name"),  // added
                            rs.getInt("level")
                    );
                    list.add(course);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error finding course with ID: " + major_id, e);
        }
        return list;
    }

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
                            rs.getTimestamp("created_at").toLocalDateTime().toLocalDate(),
                            rs.getInt("major_id"),
                            rs.getString("major_name"),  // added
                            rs.getInt("level")
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
        String sql = """
        SELECT 
            c.course_id,
            c.course_name,
            c.price,
            c.credit_score,
            c.capacity,
            c.start_date,
            c.end_date,
            c.instructor_id,
            c.room,
            c.created_at,
            c.major_id,
            c.level,
            m.major_name
        FROM course c
        JOIN major m ON c.major_id = m.major_id
        ORDER BY c.level ASC
    """;
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
                        rs.getInt("credit_score"),
                        rs.getInt("capacity"),
                        rs.getObject("start_date", LocalDate.class),
                        rs.getObject("end_date", LocalDate.class),
                        rs.getInt("instructor_id"),
                        rs.getString("room"),
                        rs.getTimestamp("created_at").toLocalDateTime().toLocalDate(),
                        rs.getInt("major_id"),
                        rs.getString("major_name"),  // added
                        rs.getInt("level")
                );
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching all courses", e);
        }

        return list;
    }
}
