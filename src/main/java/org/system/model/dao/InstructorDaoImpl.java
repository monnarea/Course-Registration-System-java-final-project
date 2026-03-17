package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.InstructorResponseDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorDaoImpl implements InstructorDao {

    // =========================================================
    //  CREATE
    // =========================================================
    @Override
    public boolean createInstructor(InstructorResponseDto instructor) {
        String sql = "INSERT INTO instructor (instructor_name, gender, age, email, phone_number, address, qualification) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, instructor.getInstructor_name());
            ps.setString(2, instructor.getGender());
            ps.setInt(3, instructor.getAge());
            ps.setString(4, instructor.getEmail());
            ps.setString(5, instructor.getPhone_number());
            ps.setString(6, instructor.getAddress());
            ps.setString(7, instructor.getQualification());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) instructor.setInstructor_id(keys.getInt(1));
                System.out.println("[CREATE] Instructor created with ID: " + instructor.getInstructor_id());
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
    public List<InstructorResponseDto> getAllInstructors() {
        List<InstructorResponseDto> list = new ArrayList<>();
        String sql = "SELECT instructor_id, instructor_name, gender, age, email, phone_number, address, qualification " +
                "FROM instructor ORDER BY instructor_id";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            System.out.println("[READ ALL] Total instructors found: " + list.size());
        } catch (SQLException e) {
            System.err.println("[READ ALL] Error: " + e.getMessage());
        }
        return list;
    }

    // =========================================================
    //  READ BY ID
    // =========================================================
    @Override
    public List<InstructorResponseDto> getInstructorById(int instructorId) {
        List<InstructorResponseDto> list = new ArrayList<>();
        String sql = "SELECT instructor_id, instructor_name, gender, age, email, phone_number, address, qualification " +
                "FROM instructor WHERE instructor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, instructorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[READ BY ID] Error: " + e.getMessage());
        }
        return list;
    }

    // =========================================================
    //  UPDATE
    // =========================================================
    @Override
    public InstructorResponseDto updateInstructor(int id, InstructorResponseDto instructor) throws SQLException {
        String sql = """
            UPDATE instructor SET
                instructor_name   = ?,
                gender         = ?,
                age  = ?,
                email      = ?,
                phone_number    = ?,
                address      = ?,
                qualification = ?
            WHERE instructor_id = ?
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1,instructor.getInstructor_name());
            pstmt.setString(2,instructor.getGender());
            pstmt.setInt(3,instructor.getAge());
            pstmt.setString(4,instructor.getEmail());
            pstmt.setString(5,instructor.getPhone_number());
            pstmt.setString(6,instructor.getAddress());
            pstmt.setString(7,instructor.getQualification());
            pstmt.setInt(8,id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed — no Instructor found with ID: " + id);
            }

            // Return the updated record
            List<InstructorResponseDto> result = getInstructorById(id);
            if (!result.isEmpty()) return result.get(0);
            throw new SQLException("Could not retrieve updated Instructor with ID: " + id);

        } catch (SQLException e) {
            throw new SQLException("Error updating Instructor with ID: " + id, e);
        }
//        StringBuilder sql = new StringBuilder("UPDATE instructor SET ");
//        List<Object> params = new ArrayList<>();
//
//        if (instructor.getInstructorName() != null) { sql.append("instructor_name=?, "); params.add(instructor.getInstructorName()); }
//        if (instructor.getGender() != null)          { sql.append("gender=?, ");          params.add(instructor.getGender()); }
//        if (instructor.getAge() > 0)                 { sql.append("age=?, ");             params.add(instructor.getAge()); }
//        if (instructor.getEmail() != null)            { sql.append("email=?, ");           params.add(instructor.getEmail()); }
//        if (instructor.getPhoneNumber() != null)      { sql.append("phone_number=?, ");    params.add(instructor.getPhoneNumber()); }
//        if (instructor.getAddress() != null)          { sql.append("address=?, ");         params.add(instructor.getAddress()); }
//        if (instructor.getQualification() != null)    { sql.append("qualification=?, ");   params.add(instructor.getQualification()); }
//
//        if (params.isEmpty()) {
//            System.err.println("[UPDATE] Nothing to update.");
//            return false;
//        }
//
//        // Remove trailing comma+space
//        String query = sql.substring(0, sql.length() - 2) + " WHERE instructor_id = ?";
//        params.add(instructor.getInstructorId());
//
//        try (Connection conn = DatabaseConfig.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//
//            for (int i = 0; i < params.size(); i++) {
//                ps.setObject(i + 1, params.get(i));
//            }
//            int rows = ps.executeUpdate();
//            System.out.println("[UPDATE] Rows affected: " + rows);
//            return rows > 0;
//        } catch (SQLException e) {
//            System.err.println("[UPDATE] Error: " + e.getMessage());
//        }
//        return false;
    }

    // =========================================================
    //  DELETE
    // =========================================================
    @Override
    public boolean deleteInstructor(int instructorId) {
        String sql = "DELETE FROM instructor WHERE instructor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, instructorId);
            int rows = ps.executeUpdate();
            System.out.println("[DELETE] Rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[DELETE] Error: " + e.getMessage());
        }
        return false;
    }

    // =========================================================
    //  HELPER
    // =========================================================
    private InstructorResponseDto mapRow(ResultSet rs) throws SQLException {
        return new InstructorResponseDto(
                rs.getInt("instructor_id"),
                rs.getString("instructor_name"),
                rs.getString("gender"),
                rs.getInt("age"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("qualification")
        );
    }
}