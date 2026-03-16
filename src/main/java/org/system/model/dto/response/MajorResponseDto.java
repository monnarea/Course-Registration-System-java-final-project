
package org.system.model.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MajorResponseDto {
    private Integer major_id;
    private String major_name;
    private String description;
}
