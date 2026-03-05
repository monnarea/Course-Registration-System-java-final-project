package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.response.SubjectResponseDto;

import javax.security.auth.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoImpl implements SubjectDao{
    @Override
    public boolean createSubject(SubjectResponseDto subject) {
        String sql = "INSERT INTO subject (sub_name, description, hour, course_id) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, subject.getSub_name());
            ps.setString(2, subject.getDescription());
            ps.setDouble  (3, subject.getHour());
            ps.setInt   (4, subject.getCourseId());

            int rows = ps.executeUpdate();

            // Retrieve the auto-generated sub_id
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    subject.setSub_id(keys.getInt(1));
                    System.out.println("[CREATE] Subject inserted successfully. sub_id = " + subject.getSub_id());
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[CREATE] Error: " + e.getMessage());
        }
        return false;
    }

    // =========================================================
    //  READ ALL
    // =========================================================
    @Override
    public
    List<SubjectResponseDto> getAllSubjects() {
        List<SubjectResponseDto> list = new ArrayList<>();
        String sql = "SELECT sub_id, sub_name, description, hour, course_id "
                + "FROM subject ORDER BY sub_id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            System.out.println("[READ ALL] Total subjects found: " + list.size());

        } catch (SQLException e) {
            System.err.println("[READ ALL] Error: " + e.getMessage());
        }
        return list;
    }

    // =========================================================
    //  READ BY ID
    // =========================================================
    @Override
    public SubjectResponseDto getSubjectById(int subId) {
        String sql = "SELECT sub_id, sub_name, description, hour, course_id "
                + "FROM subject WHERE sub_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, subId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                SubjectResponseDto s = mapRow(rs);
                System.out.println("[READ BY ID] Found: " + s);
                return s;
            } else {
                System.out.println("[READ BY ID] No subject found with sub_id = " + subId);
            }

        } catch (SQLException e) {
            System.err.println("[READ BY ID] Error: " + e.getMessage());
        }
        return null;
    }

    // =========================================================
    //  READ BY COURSE ID
    // =========================================================
    @Override
    public List<SubjectResponseDto> getSubjectsByCourseId(int courseId) {
        List<SubjectResponseDto> list = new ArrayList<>();
        String sql = "SELECT sub_id, sub_name, description, hour, course_id "
                + "FROM subject WHERE course_id = ? ORDER BY sub_id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            System.out.println("[READ BY COURSE] Subjects in course_id " + courseId + ": " + list.size());

        } catch (SQLException e) {
            System.err.println("[READ BY COURSE] Error: " + e.getMessage());
        }
        return list;
    }

    // =========================================================
    //  UPDATE
    // =========================================================
    @Override
    public boolean updateSubject(SubjectResponseDto subject) {
        String sql = "UPDATE subject "
                + "SET sub_name = ?, description = ?, hour = ?, course_id = ? "
                + "WHERE sub_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, subject.getSub_name());
            ps.setString(2, subject.getDescription());
            ps.setDouble  (3, subject.getHour());
            ps.setInt   (4, subject.getCourseId());
            ps.setInt   (5, subject.getSub_id());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[UPDATE] Subject updated successfully. sub_id = " + subject.getSub_id());
                return true;
            } else {
                System.out.println("[UPDATE] No subject found with sub_id = " + subject.getSub_id());
            }

        } catch (SQLException e) {
            System.err.println("[UPDATE] Error: " + e.getMessage());
        }
        return false;
    }

    // =========================================================
    //  DELETE
    // =========================================================
    @Override
    public boolean deleteSubject(int subId) {
        String sql = "DELETE FROM subject WHERE sub_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, subId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[DELETE] Subject deleted successfully. sub_id = " + subId);
                return true;
            } else {
                System.out.println("[DELETE] No subject found with sub_id = " + subId);
            }

        } catch (SQLException e) {
            System.err.println("[DELETE] Error: " + e.getMessage());
        }
        return false;
    }

    // =========================================================
    //  HELPER: map a ResultSet row → Subject object
    // =========================================================
    private SubjectResponseDto mapRow(ResultSet rs) throws SQLException {
        return new SubjectResponseDto(
                rs.getInt   ("sub_id"),
                rs.getString("sub_name"),
                rs.getString("description"),
                rs.getDouble  ("hour"),
                rs.getInt   ("course_id")
        );
    }
}
