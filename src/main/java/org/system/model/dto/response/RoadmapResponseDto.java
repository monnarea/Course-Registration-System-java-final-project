package org.system.model.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoadmapResponseDto {
    private Integer roadmap_id;
    private Integer major_id;
    private String major_name;
    private Integer level;
    private Integer course_id;
    private String course_name;
    private Integer sub_id;
    private String sub_name;
    private Double price;
    private Long hour;

//rs.getString("course_name"),
//        rs.getDouble("price"),
//        rs.getInt("sub_id"),
//        rs.getString("sub_name"),
//        rs.getLong("hour")
//    CREATE TABLE academic_roadmap (
//            roadmap_id SERIAL PRIMARY KEY,
//            course_id INT NOT NULL REFERENCES course(course_id) ON DELETE CASCADE,
//    sub_id INT REFERENCES subject(sub_id) ON DELETE CASCADE,
//    UNIQUE (course_id, sub_id)
//);
}
