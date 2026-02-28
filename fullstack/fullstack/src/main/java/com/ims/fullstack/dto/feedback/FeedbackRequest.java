//package com.ims.fullstack.dto.feedback;
//
//import com.ims.fullstack.model.enums.FeedbackResult;
//import jakarta.validation.constraints.*;
//
//import lombok.*;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class FeedbackRequest {
//
//    @NotNull
//    @Min(1) @Max(10)
//    private Integer technicalRating;
//
//    @NotNull
//    @Min(1) @Max(10)
//    private Integer communicationRating;
//
//    @Size(max = 2000)
//    private String comments;
//
//    @NotNull
//    private FeedbackResult result; // SELECTED / REJECTED / HOLD
//
//    // Optional: allow explicit submittedById if security is not available
//    private Long submittedById;
//}
package com.ims.fullstack.dto.feedback;

import com.ims.fullstack.model.enums.FeedbackResult;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FeedbackRequest {
    @NotNull @Min(1) @Max(10) private Integer technicalRating;
    @NotNull @Min(1) @Max(10) private Integer communicationRating;
    @Size(max = 2000) private String comments;
    @NotNull private FeedbackResult result;
    private Long submittedById;
}