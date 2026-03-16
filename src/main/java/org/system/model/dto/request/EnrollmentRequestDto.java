package org.system.model.dto.request;

import java.time.LocalDate;

import jdk.jshell.Snippet;
import lombok.*;

@Getter

@Setter

@NoArgsConstructor

@AllArgsConstructor

@ToString

@Builder

public class EnrollmentRequestDto {

    private Integer enrollment_id;

    private Integer course_id;

    private Integer student_id;

    private LocalDate enroll_date;

    private String payment_method;

    private String status;

}
