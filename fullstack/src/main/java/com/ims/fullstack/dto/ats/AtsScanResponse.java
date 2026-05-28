package com.ims.fullstack.dto.ats;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AtsScanResponse {
    private int atsScore;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private String recommendation;
}