package org.system.model.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubjectResponseDto {
    private Integer sub_id;
    private String sub_name;
    private String description;
    private Double hour;
    private int    courseId;


}
