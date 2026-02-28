package com.ims.fullstack.dto.candidate;

import lombok.Data;

@Data
public class CandidateProfileUpdateRequest {
    private String phoneNumber;
    private String location;
    private String linkedinUrl;
    private String skills;        // Comma-separated string
}