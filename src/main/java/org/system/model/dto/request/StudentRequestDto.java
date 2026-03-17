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

    private String date_of_birth;  // FIX: was "Integer age"

    private String email;

    private Integer phone_number;

    private String address;        // FIX: removed "Integer score" (not in DB schema)

    private String semester;

    private Integer year;

    private String university;

}
