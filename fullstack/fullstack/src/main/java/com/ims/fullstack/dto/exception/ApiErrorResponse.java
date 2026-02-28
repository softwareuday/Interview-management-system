//package com.ims.fullstack.dto.exception;
//
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ApiErrorResponse {
//
//    private LocalDateTime timestamp;
//    private int status;
//    private String error;
//    private String message;
//    private String path;
//}

package com.ims.fullstack.dto.exception;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}