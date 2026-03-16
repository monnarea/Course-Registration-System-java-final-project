
package org.system.model.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InstructorResponseDto {
    private Integer instructor_id;
    private String instructor_name;
    private String gender;
    private Integer age;
    private String email;
    private String phone_number;
    private String address;
    private String qualification;

}
