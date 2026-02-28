//package com.ims.fullstack.dto;
//
//
//import lombok.*;
//
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class MessageResponse {
//    private String message;
//    private Long id; // created entity id
//}

package com.ims.fullstack.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private String message;
    private Long id;
}