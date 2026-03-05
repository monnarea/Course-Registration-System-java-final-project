package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.CourseRequestDto;
import org.system.model.dto.response.CourseResponseDto;

import java.sql.*;
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
                while (rs.next()) {
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
        WHERE c.course_id = ?
        ORDER BY c.level ASC
    """;

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
        ORDER BY c.course_id ASC
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

    // ══════════════════════════════════════════════
    // CREATE
    // ══════════════════════════════════════════════
    @Override
    public CourseResponseDto create(CourseRequestDto req) throws SQLException {
        String sql = """
            INSERT INTO course
                (course_name, price, credit_score, capacity,
                 start_date, end_date, instructor_id, room, major_id, level)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, req.getCourseName());
            pstmt.setDouble(2, req.getPrice());
            pstmt.setInt(3,    req.getCreditScore());
            pstmt.setInt(4,    req.getCapacity());
            pstmt.setObject(5, req.getStartDate());
            pstmt.setObject(6, req.getEndDate());
            pstmt.setInt(7,    req.getInstructorId());
            pstmt.setString(8, req.getRoom());
            pstmt.setInt(9,    req.getMajorId());
            pstmt.setInt(10,   req.getLevel());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating course failed, no rows affected.");
            }

            // Retrieve the auto-generated course_id
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    // Fetch the full record (including joined major_name) and return it
                    List<CourseResponseDto> result = getById(newId);
                    if (!result.isEmpty()) return result.get(0);
                }
            }
            throw new SQLException("Creating course failed, no ID obtained.");

        } catch (SQLException e) {
            throw new SQLException("Error creating course: " + e.getMessage(), e);
        }
    }

    // ══════════════════════════════════════════════
    // UPDATE
    // ══════════════════════════════════════════════
    @Override
    public CourseResponseDto update(int course_id, CourseRequestDto req) throws SQLException {
        String sql = """
            UPDATE course SET
                course_name   = ?,
                price         = ?,
                credit_score  = ?,
                capacity      = ?,
                start_date    = ?,
                end_date      = ?,
                instructor_id = ?,
                room          = ?,
                major_id      = ?,
                level         = ?
            WHERE course_id = ?
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, req.getCourseName());
            pstmt.setDouble(2, req.getPrice());
            pstmt.setInt(3,    req.getCreditScore());
            pstmt.setInt(4,    req.getCapacity());
            pstmt.setObject(5, req.getStartDate());
            pstmt.setObject(6, req.getEndDate());
            pstmt.setInt(7,    req.getInstructorId());
            pstmt.setString(8, req.getRoom());
            pstmt.setInt(9,    req.getMajorId());
            pstmt.setInt(10,   req.getLevel());
            pstmt.setInt(11,   course_id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed — no course found with ID: " + course_id);
            }

            // Return the updated record
            List<CourseResponseDto> result = getById(course_id);
            if (!result.isEmpty()) return result.get(0);
            throw new SQLException("Could not retrieve updated course with ID: " + course_id);

        } catch (SQLException e) {
            throw new SQLException("Error updating course with ID: " + course_id, e);
        }
    }

    // ══════════════════════════════════════════════
    // DELETE
    // ══════════════════════════════════════════════
    @Override
    public boolean delete(int course_id) throws SQLException {
        String sql = "DELETE FROM course WHERE course_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, course_id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // true = deleted, false = ID not found

        } catch (SQLException e) {
            throw new SQLException("Error deleting course with ID: " + course_id, e);
        }
    }
}
