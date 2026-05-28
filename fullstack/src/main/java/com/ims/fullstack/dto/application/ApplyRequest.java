

package com.ims.fullstack.dto.application;

import lombok.Data;

@Data
public class ApplyRequest {
    private Long jobId;
    private String coverLetter;

    // Guest application fields
    private String guestName;
    private String guestEmail;
}