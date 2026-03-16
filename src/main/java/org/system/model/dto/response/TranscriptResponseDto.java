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

    private Integer transcript_id;

    private Integer student_id;

    private LocalDate generated_at;

}
