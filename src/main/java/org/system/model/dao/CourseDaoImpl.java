package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.CourseRequestDto;
import org.system.model.dto.response.CourseResponseDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {

    // ── shared SELECT fragment ──────────────────────────────────────────────
    private static final String SELECT_COLS = """
            SELECT
                c.course_id,
                c.course_name,
                c.price,
                c.discount,
                c.price_after_discount,
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
            """;

    /** Map one ResultSet row → CourseResponseDto */
    private CourseResponseDto map(ResultSet rs) throws SQLException {
        return new CourseResponseDto(
                rs.getInt("course_id"),
                rs.getString("course_name"),
                rs.getDouble("price"),
                rs.getDouble("discount"),
                rs.getDouble("price_after_discount"),
                rs.getInt("credit_score"),
                rs.getInt("capacity"),
                rs.getObject("start_date", LocalDate.class),
                rs.getObject("end_date",   LocalDate.class),
                rs.getInt("instructor_id"),
                rs.getString("room"),
                rs.getTimestamp("created_at").toLocalDateTime().toLocalDate(),
                rs.getInt("major_id"),
                rs.getString("major_name"),
                rs.getInt("level")
        );
    }

    // ══════════════════════════════════════════════
    // READ — all
    // ══════════════════════════════════════════════
    @Override
    public List<CourseResponseDto> getAll() throws SQLException {
        String sql = SELECT_COLS + "ORDER BY c.course_id ASC";
        List<CourseResponseDto> list = new ArrayList<>();

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs         = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new SQLException("Error fetching all courses", e);
        }
        return list;
    }

    // ══════════════════════════════════════════════
    // READ — by course_id
    // ══════════════════════════════════════════════
    @Override
    public List<CourseResponseDto> getById(int course_id) throws SQLException {
        String sql = SELECT_COLS + "WHERE c.course_id = ? ORDER BY c.level ASC";
        List<CourseResponseDto> list = new ArrayList<>();

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, course_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new SQLException("Error finding course with ID: " + course_id, e);
        }
        return list;
    }

    // ══════════════════════════════════════════════
    // READ — by major_id
    // ══════════════════════════════════════════════
    @Override
    public List<CourseResponseDto> getByMajorId(int major_id) throws SQLException {
        String sql = SELECT_COLS + "WHERE c.major_id = ? ORDER BY c.level ASC";
        List<CourseResponseDto> list = new ArrayList<>();

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, major_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new SQLException("Error finding courses for major ID: " + major_id, e);
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
                    (course_name, price, discount, credit_score, capacity,
                     start_date, end_date, instructor_id, room, major_id, level)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, req.getCourseName());
            ps.setDouble(2, req.getPrice());
            ps.setDouble(3, req.getDiscount());   // 20.0 or 50.0
            ps.setInt   (4, req.getCreditScore());
            ps.setInt   (5, req.getCapacity());
            ps.setObject(6, req.getStartDate());
            ps.setObject(7, req.getEndDate());
            ps.setInt   (8, req.getInstructorId());
            ps.setString(9, req.getRoom());
            ps.setInt   (10, req.getMajorId());
            ps.setInt   (11, req.getLevel());

            int affected = ps.executeUpdate();
            if (affected == 0) throw new SQLException("Creating course failed, no rows affected.");

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    List<CourseResponseDto> result = getById(keys.getInt(1));
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
                    discount      = ?,
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

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  req.getCourseName());
            ps.setDouble(2,  req.getPrice());
            ps.setDouble(3,  req.getDiscount());
            ps.setInt   (4,  req.getCreditScore());
            ps.setInt   (5,  req.getCapacity());
            ps.setObject(6,  req.getStartDate());
            ps.setObject(7,  req.getEndDate());
            ps.setInt   (8,  req.getInstructorId());
            ps.setString(9,  req.getRoom());
            ps.setInt   (10, req.getMajorId());
            ps.setInt   (11, req.getLevel());
            ps.setInt   (12, course_id);

            int affected = ps.executeUpdate();
            if (affected == 0)
                throw new SQLException("Update failed — no course found with ID: " + course_id);

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

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, course_id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new SQLException("Error deleting course with ID: " + course_id, e);
        }
    }
}