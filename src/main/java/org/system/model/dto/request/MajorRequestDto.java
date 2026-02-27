package org.system.model.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MajorRequestDto {
    private Integer major_id;
    private String major_name;
    private String description;

    public MajorRequestDto(String majorName, String description) {
        this.major_name=majorName;
        this.description=description;
    }
}
