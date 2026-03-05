package org.system.model.dto.request;

import lombok.*;

@Getter

@Setter

@NoArgsConstructor

@AllArgsConstructor

@ToString


public class StudentRequestDto {

    private Integer student_id;

    private String student_name;

    private String gender;

    private Integer age;

    private String email;

    private Integer phone_number;

    private Integer score;

    private String semester;

    private Integer year;

}
