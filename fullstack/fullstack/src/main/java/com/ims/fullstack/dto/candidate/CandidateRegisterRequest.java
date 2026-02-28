////package com.ims.fullstack.dto.candidate;
////
////import jakarta.validation.constraints.Email;
////import jakarta.validation.constraints.NotBlank;
////import lombok.Getter;
////import lombok.Setter;
////
////@Getter
////@Setter
////public class CandidateRegisterRequest {
////
////    @NotBlank
////    private String fullName;
////
////    @Email
////    @NotBlank
////    private String email;
////
////    @NotBlank
////    private String password;
////
////    @NotBlank
////    private String confirmPassword;
////}
//
//package com.ims.fullstack.dto.candidate;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Data;
//
//@Data
//public class CandidateRegisterRequest {
//
//    @NotBlank
//    private String fullName;   // ✅ ADDED
//
//    @Email
//    @NotBlank
//    private String email;
//
//    @NotBlank
//    private String password;
//
//    @NotBlank
//    private String confirmPassword;
//}

package com.ims.fullstack.dto.candidate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateRegisterRequest {
    @NotBlank
    private String fullName;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
}