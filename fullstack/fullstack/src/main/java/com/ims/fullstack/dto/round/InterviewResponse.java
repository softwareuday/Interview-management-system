//package com.ims.fullstack.dto.round;
//
//import com.ims.fullstack.model.InterviewMode;
//import com.ims.fullstack.model.enums.InterviewStatus;
//import lombok.*;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class InterviewResponse {
//    private Long id;
//    private Long recruiterId;
//    private Long candidateId;
//    private String candidateName;
//    private String candidateEmail;
//    private Long jobId;
//    private String position;
//    private LocalDate interviewDate;
//    private LocalTime interviewTime;
//    private InterviewMode mode;
//    private String meetingLink;
//    private String remarks;
//    private InterviewStatus status;
//    private String resumeUrl;
//}

package com.ims.fullstack.dto.round;

import com.ims.fullstack.model.enums.InterviewMode;
import com.ims.fullstack.model.enums.InterviewStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewResponse {
    private Long id;
    private Long recruiterId;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private Long jobId;
    private String position;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private InterviewMode mode;
    private String meetingLink;
    private String remarks;
    private InterviewStatus status;
    private String resumeUrl;
}