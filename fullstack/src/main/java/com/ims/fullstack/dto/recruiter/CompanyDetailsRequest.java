package com.ims.fullstack.dto.recruiter;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CompanyDetailsRequest {
    @NotBlank
    private String companyName; // must match step1 companyName

    private String website;
    private String address;
    private String companyType;

    // At least one of these must be provided
    private String cin;
    private String gstin;

    private MultipartFile incorporationCertificate; // optional file upload
}