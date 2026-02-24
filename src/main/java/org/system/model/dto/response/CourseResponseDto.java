package org.system.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDto {
    private Integer course_id;
    private String course_name;
    private Double price;
    private Integer credit_score;
    private Integer capacity;
    private LocalDate start_date;
    private LocalDate end_date;
    private Integer instructor_id;
    private String room;
    private LocalDate created_at;
    private Integer major_id;
    private String major_name;
    private Integer level;

}

