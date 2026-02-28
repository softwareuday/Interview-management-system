//package com.ims.fullstack.dto.recruiter;
//
//
//import jakarta.validation.constraints.*;
//import lombok.*;
//
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class RecruiterRegisterRequest {
//    @NotBlank @Size(min = 2, max = 100)
//    private String fullName;
//
//
//    @NotBlank @Email
//    private String email;
//
//
//    @NotBlank
//    private String company;
//
//
//    @NotBlank @Pattern(regexp = "^[0-9+\\-\\s()]{10,}$")
//    private String phone;
//
//
//    @NotBlank @Size(min = 6)
//    private String password;
//}


package com.ims.fullstack.dto.recruiter;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecruiterRegisterRequest {
    @NotBlank @Size(min = 2, max = 100)
    private String fullName;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6)
    private String password;
    @NotBlank @Pattern(regexp = "^[0-9+\\-\\s()]{10,}$")
    private String phoneNumber;      // renamed
    @NotBlank
    private String companyName;       // renamed
}