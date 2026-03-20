package org.system.model.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.Iterator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TranscriptResponseDto {

    private Integer transcriptId;
    private Integer studentId;
    private String studentName;
    private String gender;
    private LocalDate dateOfBirth;
    private String university;
    private Integer year;
    private Integer courseId;
    private String courseName;
    private String grade;
    private Double grandePoint;
    private String resultStatus;
    private LocalDate completionDate;
    private String remarks;
    private LocalDate generatedAt;


}
