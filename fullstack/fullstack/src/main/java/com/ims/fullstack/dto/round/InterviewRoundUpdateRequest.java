//package com.ims.fullstack.dto.round;
//
//import com.ims.fullstack.model.enums.RoundType;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class InterviewRoundUpdateRequest {
//    private String roundName;
//    private RoundType roundType;
//    private LocalDateTime interviewTime;
//    private String meetingLink;
//    private Boolean feedbackSubmitted;
//}
package com.ims.fullstack.dto.round;

import com.ims.fullstack.model.enums.RoundType;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewRoundUpdateRequest {
    private String roundName;
    private RoundType roundType;
    private LocalDateTime interviewTime;
    private String meetingLink;
    private Boolean feedbackSubmitted;
}