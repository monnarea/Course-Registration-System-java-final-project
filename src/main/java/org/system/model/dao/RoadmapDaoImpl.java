package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.RoadmapRequestDto;
import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoadmapDaoImpl implements RoadmapDao {

    // ── shared SELECT fragment ──────────────────────────────────────────────
    private static final String SELECT_COLS = """
            SELECT
                ar.roadmap_id,
                c.major_id,
                m.major_name,
                c.level,
                c.course_id,
                c.course_name,
                c.price,
                c.discount,
                c.price_after_discount,
                c.capacity,
                s.sub_id,
                s.sub_name,
                s.hour
            FROM academic_roadmap ar
            JOIN course  c ON ar.course_id = c.course_id
            JOIN subject s ON ar.sub_id    = s.sub_id
            JOIN major   m ON c.major_id   = m.major_id
            """;

    /** Map one ResultSet row → RoadmapResponseDto */
    private RoadmapResponseDto map(ResultSet rs) throws SQLException {
        return new RoadmapResponseDto(
                rs.getInt("roadmap_id"),
                rs.getInt("major_id"),
                rs.getString("major_name"),
                rs.getInt("level"),
                rs.getInt("course_id"),
                rs.getString("course_name"),
                rs.getInt("sub_id"),
                rs.getString("sub_name"),
                rs.getInt("capacity"),
                rs.getDouble("price"),
                rs.getDouble("discount"),
                rs.getDouble("price_after_discount"),
                rs.getLong("hour")
        );
    }

    // ══════════════════════════════════════════════
    // READ — all
    // ══════════════════════════════════════════════
    @Override
    public List<RoadmapResponseDto> getAll() throws SQLException {
        String sql = SELECT_COLS + "ORDER BY m.major_id ASC, c.level ASC, s.sub_id ASC";
        List<RoadmapResponseDto> list = new ArrayList<>();

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs         = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new SQLException("Error fetching roadmap data", e);
        }
        return list;
    }

    // ══════════════════════════════════════════════
    // READ — by major_id
    // ══════════════════════════════════════════════
    @Override
    public List<RoadmapResponseDto> getByMajorId(int major_id) throws SQLException {
        String sql = SELECT_COLS + "WHERE c.major_id = ? ORDER BY c.level ASC, s.sub_id ASC";
        List<RoadmapResponseDto> list = new ArrayList<>();

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, major_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new SQLException("Error finding roadmap for major ID: " + major_id, e);
        }
        return list;
    }

    // ══════════════════════════════════════════════
    // READ — by roadmap_id
    // ══════════════════════════════════════════════
    @Override
    public List<RoadmapResponseDto> getById(int id) throws SQLException {
        String sql = SELECT_COLS + "WHERE ar.roadmap_id = ?";
        List<RoadmapResponseDto> list = new ArrayList<>();

        try (Connection conn  = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new SQLException("Error finding roadmap with ID: " + id, e);
        }
        return list;
    }

    @Override
    public RoadmapResponseDto create( RoadmapRequestDto request) throws SQLException {


        String sql = """
        INSERT INTO academic_roadmap (course_id, sub_id,major_id )
        VALUES (?, ?, ?)
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, request.getCourseId());
            pstmt.setInt(2, request.getSubId());
            pstmt.setInt(3, request.getMajorId());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating roadmap failed.");
            }

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    List<RoadmapResponseDto> result = getById(newId); // ✅ List
                    if (!result.isEmpty()) {
                        return result.get(0);                          // ✅ first element
                    }
                }
            }

            throw new SQLException("No ID obtained.");
        }
    }
    @Override
    public RoadmapResponseDto update(int roadmap_id, RoadmapRequestDto request) throws SQLException {
        String sql = """
        UPDATE academic_roadmap SET
            course_id = ?,
            sub_id    = ?,
            major_id  = ?
        WHERE roadmap_id = ?
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, request.getCourseId());
            pstmt.setInt(2, request.getSubId());
            pstmt.setInt(3, request.getMajorId());
            pstmt.setInt(4, roadmap_id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed — no roadmap found with ID: " + roadmap_id);
            }

            // Return updated record
            List<RoadmapResponseDto> result = getById(roadmap_id);
            if (!result.isEmpty()) return result.get(0);

            throw new SQLException("Could not retrieve updated roadmap with ID: " + roadmap_id);

        } catch (SQLException e) {
            throw new SQLException("Error updating roadmap with ID: " + roadmap_id, e);
        }
    }
    @Override
    public boolean delete(int roadmapId) throws SQLException {
        String sql = "DELETE FROM academic_roadmap WHERE roadmap_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roadmapId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new SQLException("Error deleting course with ID: " + roadmapId, e);
        }
    }
}
