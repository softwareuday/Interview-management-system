
package com.ims.fullstack.dto.round;

import com.ims.fullstack.model.enums.RoundType;
import com.ims.fullstack.model.enums.InterviewStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewRoundResponse {
    private Long id;
    private String roundName;
    private RoundType roundType;
    private LocalDateTime interviewTime;
    private String meetingLink;
    private InterviewStatus status;
    private Boolean feedbackSubmitted;
    private Long candidateId;
    private Long jobId;
    private Long recruiterId;
}