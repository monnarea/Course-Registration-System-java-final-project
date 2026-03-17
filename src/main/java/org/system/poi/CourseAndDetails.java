package org.system.poi;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CourseAndDetails {
    private Integer course_id;
    private String course_name;
    private Double price;
    private String description;
    private Integer credit_score;
    private Integer capacity;
    private String  start_date;
    private String end_date;
    private Integer instructor_id;
    private String room;
    private Integer major_id;
    private Integer level;
    private String day_of_week;
    private String morning;
    private String afternoon;
    private String evening;
}