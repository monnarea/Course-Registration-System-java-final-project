package org.system.model.dto.response;

import lombok.*;

@Getter

@Setter

@AllArgsConstructor

@NoArgsConstructor

@ToString

public class StudentResponseDto {

    private Integer student_id;

    private String student_name;

    private String gender;

    private Integer age;

    private String email;

    private Integer phone_number;

    private Integer score;

    private String address;

    private String semester;

    private Integer year;

}
