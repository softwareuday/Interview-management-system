
package com.ims.fullstack.dto.application;

import com.ims.fullstack.model.enums.ApplicationStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
    private Long applicationId;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private Long jobId;
    private String jobTitle;
    private String resumeUrl;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private Integer atsScore;
    private String matchedKeywords;
    private String missingKeywords;
}