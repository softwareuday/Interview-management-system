//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.service.MailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/test-mail")
//@RequiredArgsConstructor
//public class MailTestController {
//
//    private final MailService mailService;
//
//    @PostMapping
//    public ResponseEntity<String> sendTestMail(
//            @RequestParam String to,
//            @RequestParam String subject,
//            @RequestParam String body
//    ) {
//        mailService.sendInterviewInvite(to, subject, body, null);
//        return ResponseEntity.ok("Mail sent!");
//    }
//}
package com.ims.fullstack.controller;

import com.ims.fullstack.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-mail")
@RequiredArgsConstructor
public class MailTestController {
    private final MailService mailService;

    @PostMapping
    public ResponseEntity<String> sendTestMail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        mailService.sendInterviewInvite(to, subject, body);
        return ResponseEntity.ok("Mail sent!");
    }
}