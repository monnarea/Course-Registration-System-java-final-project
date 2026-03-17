package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.EnrollmentRequestDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDaoImpl implements EnrollmentDao {

    // ── INSERT ────────────────────────────────────────────────────────────────
    @Override
    public boolean insertEnrollment(EnrollmentRequestDto e) {
        // FIX: SQL had 2 columns but 4 placeholders — now all 5 fields included
        String sql = "INSERT INTO enrollments (course_id, student_id, enrollment_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt   (1, e.getCourse_id());
            ps.setInt   (2, e.getStudent_id());
            ps.setDate  (3, Date.valueOf(e.getEnrollment_date() != null ? e.getEnrollment_date() : LocalDate.now()));
;
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setEnrollment_id(keys.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ── SELECT BY ID ──────────────────────────────────────────────────────────
    @Override
    public EnrollmentRequestDto getEnrollmentById(int id) {
        String sql = "SELECT * FROM enrollments WHERE enrollment_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── SELECT BY STUDENT ID ──────────────────────────────────────────────────
    @Override
    public List<EnrollmentRequestDto> getEnrollmentsByStudentId(int studentId) {
        List<EnrollmentRequestDto> list = new ArrayList<>();
        // FIX: was filtering by enrollment_id instead of student_id
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── SELECT ALL ────────────────────────────────────────────────────────────
    @Override
    public List<EnrollmentRequestDto> getAllEnrollments() {
        List<EnrollmentRequestDto> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @Override
    public boolean updateEnrollment(EnrollmentRequestDto e) {
        // FIX: SQL had 2 SET columns but was setting 4 parameters — now all fields included
        String sql = "UPDATE enrollments SET course_id=?, student_id=?, enrollment_date=? " +
                "WHERE enrollment_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, e.getCourse_id());
            ps.setInt   (2, e.getStudent_id());
            ps.setDate  (3, Date.valueOf(e.getEnrollment_date() != null ? e.getEnrollment_date() : LocalDate.now()));
            ps.setInt   (6, e.getEnrollment_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public boolean deleteEnrollment(int id) {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── HELPER ────────────────────────────────────────────────────────────────
    private EnrollmentRequestDto mapRow(ResultSet rs) throws SQLException {
        return EnrollmentRequestDto.builder()
                .enrollment_id(rs.getInt("enrollment_id"))   // FIX: was "student Id" (wrong column + space)
                .course_id(rs.getInt("course_id"))           // FIX: was parsing via getString then Integer.valueOf
                .student_id(rs.getInt("student_id"))
                .enrollment_date(rs.getDate("enroll_date") != null
                        ? rs.getDate("enroll_date").toLocalDate() : null)

                .build();
    }
}