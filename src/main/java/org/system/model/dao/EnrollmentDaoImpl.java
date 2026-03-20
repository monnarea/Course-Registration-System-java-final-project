package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.EnrollmentRequestDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDaoImpl implements EnrollmentDao {

    // ── INSERT ────────────────────────────────────────────────────────────────
    @Override
    public boolean insertEnrollment(EnrollmentRequestDto e) {
        String sql = "INSERT INTO enrollments (student_id, course_id, enrollment_date, shift) VALUES (?, ?, NOW(), ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong  (1, e.getStudent_id());
            ps.setInt   (2, e.getCourse_id());
            ps.setString(3, e.getShift());
            return ps.executeUpdate() > 0;

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
    public List<EnrollmentRequestDto> getEnrollmentsByStudentId(long studentId) {
        List<EnrollmentRequestDto> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, studentId);
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
        String sql = "SELECT * FROM enrollments ORDER BY enrollment_id";
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
        EnrollmentRequestDto existing = getEnrollmentById(e.getEnrollment_id());
        if (existing == null) return false;

        String sql = "UPDATE enrollments SET course_id=?, student_id=?, enrollment_date=?, shift=? WHERE enrollment_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt      (1, e.getCourse_id());
            ps.setLong     (2, existing.getStudent_id());
            ps.setTimestamp(3, existing.getEnrolled_at() != null
                    ? Timestamp.valueOf(existing.getEnrolled_at())
                    : new Timestamp(System.currentTimeMillis()));
            ps.setString   (4, e.getShift() != null ? e.getShift() : existing.getShift());
            ps.setInt      (5, e.getEnrollment_id());
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
        Timestamp ts = rs.getTimestamp("enrollment_date");
        return EnrollmentRequestDto.builder()
                .enrollment_id(rs.getInt   ("enrollment_id"))
                .course_id    (rs.getInt   ("course_id"))
                .student_id   (rs.getLong  ("student_id"))
                .shift        (rs.getString("shift"))
                .enrolled_at  (ts != null ? ts.toLocalDateTime() : null)
                .build();
    }
}