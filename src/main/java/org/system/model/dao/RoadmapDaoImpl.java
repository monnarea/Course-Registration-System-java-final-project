package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoadmapDaoImpl implements RoadmapDao {

    @Override
    public List<RoadmapResponseDto> getAll() throws SQLException {

        String sql = """
        SELECT 
            ar.roadmap_id,
            c.major_id,
            m.major_id,
            c.level,
            m.major_name,
            c.course_id,
            c.course_name,
            c.price,
            s.sub_id,
            s.sub_name,
            s.hour
        FROM academic_roadmap ar
        JOIN course c ON ar.course_id = c.course_id
        JOIN subject s ON ar.sub_id = s.sub_id
        JOIN major m ON c.major_id = m.major_id
        """;

        List<RoadmapResponseDto> list = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                RoadmapResponseDto dto = new RoadmapResponseDto(
                        rs.getInt("roadmap_id"),
                        rs.getInt("major_id"),
                        rs.getString("major_name"),
                        rs.getInt("level"),
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("sub_id"),
                        rs.getString("sub_name"),
                        rs.getDouble("price"),
                        rs.getLong("hour")
                );

                list.add(dto);
            }

        } catch (SQLException e) {
            throw new SQLException("Error fetching roadmap data", e);
        }

        return list;
    }


    @Override
    public List<RoadmapResponseDto> getById(int major_id) throws SQLException {

        String sql = """
        SELECT 
            ar.roadmap_id,
            c.major_id,
            m.major_name,
            c.level,
            c.course_id,
            c.course_name,
            c.price,
            s.sub_id,
            s.sub_name,
            s.hour
        FROM academic_roadmap ar
        JOIN course c ON ar.course_id = c.course_id
        JOIN subject s ON ar.sub_id = s.sub_id
        JOIN major m ON c.major_id = m.major_id
        WHERE c.major_id = ?
        ORDER BY c.level ASC
        """;

        List<RoadmapResponseDto> list = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, major_id);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    RoadmapResponseDto dto = new RoadmapResponseDto(
                            rs.getInt("roadmap_id"),
                            rs.getInt("major_id"),
                            rs.getString("major_name"),
                            rs.getInt("level"),
                            rs.getInt("course_id"),
                            rs.getString("course_name"),
                            rs.getInt("sub_id"),
                            rs.getString("sub_name"),
                            rs.getDouble("price"),
                            rs.getLong("hour")

                    );

                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error finding roadmap with ID: " + major_id, e);
        }

        return list;
    }

}
