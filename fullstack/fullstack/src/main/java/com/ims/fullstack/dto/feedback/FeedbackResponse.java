//package com.ims.fullstack.dto.feedback;
//
//import com.ims.fullstack.model.enums.FeedbackResult;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class FeedbackResponse {
//    private Long id;
//    private Integer technicalRating;
//    private Integer communicationRating;
//    private String comments;
//    private FeedbackResult result;
//    private Long interviewRoundId;
//    private Long submittedById;
//    private LocalDateTime submittedAt;
//}
package com.ims.fullstack.dto.feedback;

import com.ims.fullstack.model.enums.FeedbackResult;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FeedbackResponse {
    private Long id;
    private Integer technicalRating;
    private Integer communicationRating;
    private String comments;
    private FeedbackResult result;
    private Long interviewRoundId;
    private Long submittedById;
    private LocalDateTime submittedAt;
}