
package com.ims.fullstack.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AtsScoreResult {
    private int score;
    private List<String> matched;
    private List<String> missing;
}