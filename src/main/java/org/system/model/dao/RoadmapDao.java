package org.system.model.dao;

import org.system.model.dto.response.RoadmapResponseDto;

import java.util.List;

public interface RoadmapDao {
    List<RoadmapResponseDto> webDevelopment();
    List<RoadmapResponseDto> CyberSecurity();
    List<RoadmapResponseDto> DataAnalytics();
    List<RoadmapResponseDto> NetworkingAndSecurity();
    List<RoadmapResponseDto> SpringMicroservices();
    List<RoadmapResponseDto> DevOpsEngineering();
    List<RoadmapResponseDto> ArtificialIntelligence();


}
