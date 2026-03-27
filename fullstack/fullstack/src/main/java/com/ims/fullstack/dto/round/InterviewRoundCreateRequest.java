
package com.ims.fullstack.dto.round;

import com.ims.fullstack.model.enums.RoundType;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewRoundCreateRequest {
    @NotNull private Long jobId;
    @NotNull private Long candidateId;
    @NotNull private Long recruiterId;
    @NotBlank private String roundName;
    private RoundType roundType = RoundType.ONLINE;
    @NotNull private LocalDateTime interviewTime;
    private String meetingLink;
}