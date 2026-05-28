
package com.ims.fullstack.dto.ats;

import lombok.Data;

@Data
public class AtsScanRequest {
    private Long jobId;
    private Long candidateId;
}