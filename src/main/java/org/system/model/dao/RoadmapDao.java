package org.system.model.dao;

import org.system.model.dto.request.RoadmapRequestDto;
import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface RoadmapDao {
    List<RoadmapResponseDto> getAll() throws SQLException;
    List<RoadmapResponseDto> getByMajorId(int major_id) throws SQLException;
    List<RoadmapResponseDto> getById(int major_id) throws SQLException;
    RoadmapResponseDto create(RoadmapRequestDto request) throws SQLException;
    boolean delete(int roadMap_id) throws SQLException;

}
