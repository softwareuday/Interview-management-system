

package com.ims.fullstack.dto.ats;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtsScoreResponse {
    private Integer score;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private String recommendation;
}