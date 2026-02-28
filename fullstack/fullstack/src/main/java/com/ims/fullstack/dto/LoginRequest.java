////package com.ims.fullstack.dto;
////
////import jakarta.validation.constraints.*;
////import lombok.*;
////
////@Getter
////@Setter
////@NoArgsConstructor
////@AllArgsConstructor
////@Builder
////public class LoginRequest {
////    @NotBlank @Email
////    private String email;
////
////    @NotBlank
////    private String password;
////}
////package com.ims.fullstack.dto;
////
////import lombok.Getter;
////import lombok.Setter;
////
////@Getter
////@Setter
////public class LoginRequest {
////    private String email;
////    private String password;
////}
//
//
//package com.ims.fullstack.dto;
//
//import lombok.Data;
//
//@Data
//public class LoginRequest {
//    private String email;
//    private String password;
//}
package com.ims.fullstack.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}