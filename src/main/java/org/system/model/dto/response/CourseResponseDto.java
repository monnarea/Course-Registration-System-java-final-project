
package org.system.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDto {
    private Integer course_id;
    private String  course_name;
    private Double  price;
    private Double  discount;             // e.g. 20.0 or 50.0  (percent)
    private Double  price_after_discount; // computed column from DB
    private Integer credit_score;
    private Integer capacity;
    private LocalDate start_date;
    private LocalDate end_date;
    private Integer instructor_id;
    private String  room;
    private LocalDate created_at;
    private Integer major_id;
    private String  major_name;
    private Integer level;
}
