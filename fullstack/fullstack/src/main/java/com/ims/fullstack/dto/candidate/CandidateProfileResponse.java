package com.ims.fullstack.dto.candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfileResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String location;
    private String linkedinUrl;
    private String skills;        // Comma-separated string
    private String resumeUrl;
}