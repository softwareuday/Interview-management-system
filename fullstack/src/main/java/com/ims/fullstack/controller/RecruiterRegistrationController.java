package com.ims.fullstack.controller;

import com.ims.fullstack.dto.MessageResponse;
import com.ims.fullstack.dto.recruiter.OtpVerificationRequest;
import com.ims.fullstack.dto.recruiter.RecruiterStep1Request;
import com.ims.fullstack.service.RecruiterAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/recruiter")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RecruiterRegistrationController {

    private final RecruiterAuthService recruiterAuthService;

    @PostMapping("/register/step1")
    public ResponseEntity<MessageResponse> registerStep1(@Valid @RequestBody RecruiterStep1Request request) {
        recruiterAuthService.registerStep1(request);
        return ResponseEntity.ok(new MessageResponse("OTP sent to your email. Please verify to continue.", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<MessageResponse> verifyOtp(@Valid @RequestBody OtpVerificationRequest request) {
        recruiterAuthService.verifyOtp(request.getEmail(), request.getOtpCode());
        return ResponseEntity.ok(new MessageResponse("Email verified successfully. Proceed to company details.", null));
    }
}