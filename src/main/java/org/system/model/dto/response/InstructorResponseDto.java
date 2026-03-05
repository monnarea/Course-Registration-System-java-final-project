package org.system.model.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InstructorResponseDto {
    private int instructorId;
    private String instructorName;
    private String gender;
    private int age;
    private String email;
    private String phoneNumber;
    private String address;
    private String qualification;

}
