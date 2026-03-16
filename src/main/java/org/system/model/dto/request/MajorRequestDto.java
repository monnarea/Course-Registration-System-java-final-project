
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

}
