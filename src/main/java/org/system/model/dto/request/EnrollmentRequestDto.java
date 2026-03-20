package org.system.model.dto.request;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EnrollmentRequestDto {
    private Integer       enrollment_id;
    private Integer       course_id;
    private Long          student_id;    // bigint in DB → Long in Java
    private LocalDate     enrollment_date;
    private LocalDateTime enrolled_at;
    private String shift;
}