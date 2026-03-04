package org.system.model.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseTimeResponseDto {
    private int  time_id ;
    private int course_id;
    private String day_of_week;
    private String morning;
    private String evening;
    private String afternoon;

    public CourseTimeResponseDto(Integer courseId, String dayOfWeek, String morning, String afternoon, String evening) {
            this.course_id=courseId;
            this.day_of_week= dayOfWeek;
            this.morning=morning;
            this.afternoon=afternoon;
            this.evening=evening;
    }
}
