package org.system.model.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.Calendar;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString


public class CourseRequestDto {
    private String courseName;
    private double price;
    private int creditScore;
    private int capacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private int instructorId;
    private String room;
    private int majorId;
    private int level;


}
