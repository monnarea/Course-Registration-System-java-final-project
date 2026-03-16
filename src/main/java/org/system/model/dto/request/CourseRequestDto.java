
package org.system.model.dto.request;

import lombok.*;

import java.time.LocalDate;

@Setter

@Getter

@AllArgsConstructor

@NoArgsConstructor

@ToString

public class CourseRequestDto {

    private String    courseName;

    private double    price;

    private double    discount;
    // 20.0 or 50.0  — price_after_discount is GENERATED in DB

    private int       creditScore;

    private int       capacity;

    private LocalDate startDate;

    private LocalDate endDate;

    private int       instructorId;

    private String    room;

    private int       majorId;

    private int       level;

}
