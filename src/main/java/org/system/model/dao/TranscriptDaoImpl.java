
package org.system.model.dao;

import org.system.model.dto.response.TranscriptResponseDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TranscriptDaoImpl implements TranscriptDao {

    private final Connection connection;

    public TranscriptDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(TranscriptResponseDto transcript) {
        String sql = "INSERT INTO transcript (student_id, generated_at) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, transcript.getStudent_id());
            ps.setDate(2, Date.valueOf(transcript.getGenerated_at()));

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
        String sql = "SELECT * FROM transcript";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TranscriptResponseDto transcript = new TranscriptResponseDto(
                        rs.getInt("transcript_id"),
                        rs.getInt("student_id"),
                        rs.getDate("generated_at").toLocalDate()
                );

                list.add(transcript);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<TranscriptResponseDto> findById(int id) {
        String sql = "SELECT * FROM transcript WHERE transcript_id = ?";
        List<TranscriptResponseDto> list = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    TranscriptResponseDto transcript = new TranscriptResponseDto(
                            rs.getInt("transcript_id"),
                            rs.getInt("student_id"),
                            rs.getDate("generated_at").toLocalDate()
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean update(TranscriptResponseDto transcript) {
        String sql = "UPDATE transcript SET student_id = ?, generated_at = ? WHERE transcript_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, transcript.getStudent_id());
            ps.setDate(2, Date.valueOf(transcript.getGenerated_at()));
            ps.setInt(3, transcript.getTranscript_id());

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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
