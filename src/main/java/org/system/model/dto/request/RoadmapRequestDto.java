package org.system.model.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapRequestDto {

    private int courseId;
    private int subId;
    private int majorId;
}