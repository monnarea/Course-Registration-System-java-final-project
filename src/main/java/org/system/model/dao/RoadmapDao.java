package org.system.model.dao;

import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface RoadmapDao {
    List<RoadmapResponseDto> getAll() throws SQLException;
    List<RoadmapResponseDto> getById(int major_id) throws SQLException;
}
