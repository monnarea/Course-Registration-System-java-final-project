
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
    private Integer capacity;
    private Double price;
    private Double  discount;
    private Double  price_after_discount;
    private Long hour;

}
