package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.response.TranscriptResponseDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TranscriptDaoImpl implements TranscriptDao {

    @Override
    public boolean insert(TranscriptResponseDto transcript) {
        String sql = """
                INSERT INTO transcript (student_id, course_id, generated_at, grade, grade_point, result_status, completion_date, remarks)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, transcript.getStudentId());
            ps.setInt(2, transcript.getCourseId());
            ps.setTimestamp(3, Timestamp.valueOf(transcript.getGeneratedAt().atStartOfDay()));
            ps.setString(4, transcript.getGrade());
            ps.setDouble(5, transcript.getGrandePoint());
            ps.setString(6, transcript.getResultStatus());
            ps.setDate(7, transcript.getCompletionDate() != null
                    ? Date.valueOf(transcript.getCompletionDate()) : null);
            ps.setString(8, transcript.getRemarks());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<TranscriptResponseDto> findAll() {
        List<TranscriptResponseDto> list = new ArrayList<>();
        String sql = """
                SELECT
                    t.transcript_id,
                    t.generated_at,
                    t.grade,
                    t.grade_point,
                    t.result_status,
                    t.completion_date,
                    t.remarks,
                    s.id            AS student_id,
                    s.student_name,
                    s.gender,
                    s.date_of_birth,
                    s.email,
                    s.phone_number,
                    s.address,
                    s.semester,
                    s.year,
                    s.university,
                    c.course_id,
                    c.course_name
                FROM transcript t
                JOIN student s ON s.id        = t.student_id
                JOIN course  c ON c.course_id = t.course_id
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TranscriptResponseDto transcript = mapRow(rs);
                list.add(transcript);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<TranscriptResponseDto> findById(int id) {
        String sql = """
                SELECT
                    t.transcript_id,
                    t.generated_at,
                    t.grade,
                    t.grade_point,
                    t.result_status,
                    t.completion_date,
                    t.remarks,
                    s.id            AS student_id,
                    s.student_name,
                    s.gender,
                    s.date_of_birth,
                    s.email,
                    s.phone_number,
                    s.address,
                    s.semester,
                    s.year,
                    s.university,
                    c.course_id,
                    c.course_name
                FROM transcript t
                JOIN student s ON s.id        = t.student_id
                JOIN course  c ON c.course_id = t.course_id
                WHERE t.transcript_id = ?
                """;
        List<TranscriptResponseDto> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean update(TranscriptResponseDto transcript) {
        String sql = """
                UPDATE transcript
                SET student_id      = ?,
                    course_id       = ?,
                    generated_at    = ?,
                    grade           = ?,
                    grade_point     = ?,
                    result_status   = ?,
                    completion_date = ?,
                    remarks         = ?
                WHERE transcript_id = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, transcript.getStudentId());
            ps.setInt(2, transcript.getCourseId());
            ps.setTimestamp(3, Timestamp.valueOf(transcript.getGeneratedAt().atStartOfDay()));
            ps.setString(4, transcript.getGrade());
            ps.setDouble(5, transcript.getGrandePoint());
            ps.setString(6, transcript.getResultStatus());
            ps.setDate(7, transcript.getCompletionDate() != null
                    ? Date.valueOf(transcript.getCompletionDate()) : null);
            ps.setString(8, transcript.getRemarks());
            ps.setInt(9, transcript.getTranscriptId());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM transcript WHERE transcript_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── helper ──────────────────────────────────────────────────────────────
    private TranscriptResponseDto mapRow(ResultSet rs) throws SQLException {
        TranscriptResponseDto dto = new TranscriptResponseDto();

        dto.setTranscriptId(rs.getInt("transcript_id"));
        dto.setGeneratedAt(LocalDate.from(rs.getTimestamp("generated_at").toLocalDateTime()));
        dto.setGrade(rs.getString("grade"));
        dto.setGrandePoint(rs.getDouble("grade_point"));
        dto.setResultStatus(rs.getString("result_status"));

        Date completionDate = rs.getDate("completion_date");
        if (completionDate != null) {
            dto.setCompletionDate(completionDate.toLocalDate());
        }
        dto.setRemarks(rs.getString("remarks"));

        // Student fields
        dto.setStudentId(rs.getInt("student_id"));
        dto.setStudentName(rs.getString("student_name"));
        dto.setGender(rs.getString("gender"));
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) dto.setDateOfBirth(dob.toLocalDate());
        dto.setYear(Integer.valueOf(rs.getString("year")));
        dto.setUniversity(rs.getString("university"));

        // Course fields
        dto.setCourseId(rs.getInt("course_id"));
        dto.setCourseName(rs.getString("course_name"));

        return dto;
    }
}