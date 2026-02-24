package org.system.service;

import org.system.model.dao.RoadmapDao;
import org.system.model.dao.RoadmapDaoImpl;
import org.system.model.dto.response.RoadmapResponseDto;

import java.sql.SQLException;
import java.util.List;

import static org.system.view.View.printRoadmapTable;
import static org.system.view.View.printSingleRoadmapTable;

public class RoadmapService {

    private final RoadmapDao roadmapDao = new RoadmapDaoImpl();
    private final RoadmapDao roadmapDaoByid = new RoadmapDaoImpl();

    public void diplayAllRoadmap(){
        try {
            List<RoadmapResponseDto> allroadmap = roadmapDao.getAll();
            if (allroadmap.isEmpty()) {
                System.out.println("No courses found in database.");
            } else {
                System.out.println("Total courses found: " +allroadmap.size());


                printRoadmapTable(allroadmap);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void diplaySingleRoadmap(int major_id){
        try {
            List<RoadmapResponseDto> allroadmap1 = roadmapDaoByid.getById(major_id);
            if (allroadmap1.isEmpty()) {
                System.out.println("No courses found in database.");
            } else {
                System.out.println("Total courses found: " +allroadmap1.size());


                printSingleRoadmapTable(allroadmap1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
