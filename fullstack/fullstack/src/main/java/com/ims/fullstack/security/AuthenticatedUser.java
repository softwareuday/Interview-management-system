//package com.ims.fullstack.security;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Getter
//@AllArgsConstructor
//public class AuthenticatedUser {
//    private Long id;
//    private String email;
//    private String role;
//}
package com.ims.fullstack.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUser {
    private Long id;
    private String email;
    private String role;
}