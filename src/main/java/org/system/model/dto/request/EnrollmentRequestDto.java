package org.system.model.dto.request;

import lombok.*;
import java.time.LocalDate;

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
    private LocalDate enrollment_date;

}