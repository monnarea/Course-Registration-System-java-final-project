
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
        String sql = "INSERT INTO ero_table (course_id, student_id, payment_method, status) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getCourse_id().toString());
            ps.setInt   (2, e.getStudent_id());
            ps.setString(3, e.getPayment_method());
            ps.setString(4, e.getStatus());
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
        String sql = "SELECT * FROM ero_table WHERE id = ?";
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
        String sql = "SELECT * FROM ero_table WHERE student_id = ?";
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
        String sql = "SELECT * FROM ero_table";
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
        String sql = "UPDATE ero_table SET course_id=?, student_id=?, payment_method=?, status=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getCourse_id().toString());
            ps.setInt   (2, e.getStudent_id());
            ps.setString(3, e.getPayment_method());
            ps.setString(4, e.getStatus());
            ps.setInt   (5, e.getEnrollment_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public boolean deleteEnrollment(int id) {
        String sql = "DELETE FROM ero_table WHERE id = ?";
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
                .enrollment_id(rs.getInt("id"))
                .course_id(Integer.valueOf(rs.getString("course_id")))
                .student_id(rs.getInt("student_id"))
                .payment_method(rs.getString("payment_method"))
                .status(rs.getString("status"))
                .build();
    }
}
