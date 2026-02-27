package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.MajorRequestDto;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.MajorResponseDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MajorDaoImpl implements MajorDao{
    @Override
    public List<MajorResponseDto> getAll() throws SQLException{
        List<MajorResponseDto> list = new ArrayList<>();
        String sql = "select * from major";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MajorResponseDto major = new MajorResponseDto(
                        rs.getInt("major_id"),
                        rs.getString("major_name"),
                        rs.getString("description")
                );
                list.add(major);
            }
            System.out.println("[READ ALL] Total Major found: " + list.size());
        } catch (SQLException e) {
            System.err.println("[READ ALL] Error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<MajorResponseDto> getById(int majorId) throws SQLException {

        List<MajorResponseDto> list = new ArrayList<>();
        String sql = "select * from major where major_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, majorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MajorResponseDto major = new MajorResponseDto(
                        rs.getInt("major_id"),
                        rs.getString("major_name"),
                        rs.getString("description")
                );
                list.add(major);

            }
        } catch (SQLException e) {
            throw new SQLException("Error finding Major with ID: " + majorId, e);
        }
        return list;
    }

    @Override
    public MajorResponseDto create(MajorRequestDto majorRequestDto) throws SQLException{
        String sql = """
            INSERT INTO major
                (major_name,description)
            VALUES (?, ?)
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1,majorRequestDto.getMajor_name());
            pstmt.setString(2,majorRequestDto.getDescription());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating major failed, no rows affected.");
            }

            // Retrieve the auto-generated course_id
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    // Fetch the full record (including joined major_name) and return it
                    List<MajorResponseDto> result = getById(newId);
                    if (!result.isEmpty()) return result.get(0);
                }
            }
            throw new SQLException("Creating major failed, no ID obtained.");

        } catch (SQLException e) {
            throw new SQLException("Error creating major: " + e.getMessage(), e);
        }
    }

    @Override
    public MajorResponseDto update(int major_id, MajorRequestDto majorRequestDto) throws SQLException {
        String sql = """
            UPDATE major SET
                major_name=?,
                description=?
            WHERE major_id = ?
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, majorRequestDto.getMajor_name());
            pstmt.setString(2, majorRequestDto.getDescription());
            pstmt.setInt(3, major_id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed — no major found with ID: " + major_id);
            }

            // Return the updated record
            List<MajorResponseDto> result = getById(major_id);

            if (!result.isEmpty()) return result.get(0);

            throw new SQLException("Could not retrieve updated major with ID: " + major_id);

        } catch (SQLException e) {
            throw new SQLException("Error updating major with ID: " + major_id, e);
        }
    }

    @Override
    public boolean delete(int major_id) throws SQLException {
        String sql = "DELETE FROM major WHERE major_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, major_id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // true = deleted, false = ID not found

        } catch (SQLException e) {
            throw new SQLException("Error deleting course with ID: " + major_id, e);
        }
    }
}
