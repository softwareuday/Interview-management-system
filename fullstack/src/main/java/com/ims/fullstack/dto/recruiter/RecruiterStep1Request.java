package com.ims.fullstack.dto.recruiter;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RecruiterStep1Request {
    @NotBlank @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank @Email
    private String email;

    @NotBlank @Pattern(regexp = "^[0-9+\\-\\s()]{10,}$")
    private String phoneNumber;

    @NotBlank
    private String companyName; // will be confirmed later

    @NotBlank @Size(min = 6)
    private String password;
}