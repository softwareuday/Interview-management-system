

package com.ims.fullstack.dto.round;

import com.ims.fullstack.model.enums.InterviewMode;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequest {
    private Long candidateId;
    private Long jobId;
    private String position;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private InterviewMode mode;
    private String meetingLink;
    private String remarks;
}