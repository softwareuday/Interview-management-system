

package com.ims.fullstack.controller;

import com.ims.fullstack.dto.AuthResponse;
import com.ims.fullstack.dto.LoginRequest;
import com.ims.fullstack.dto.MessageResponse;
import com.ims.fullstack.dto.recruiter.RecruiterRegisterRequest;
import com.ims.fullstack.service.RecruiterAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/recruiter")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RecruiterAuthController {
    private final RecruiterAuthService recruiterAuthService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RecruiterRegisterRequest request) {
        recruiterAuthService.register(request);
        return ResponseEntity.ok(new MessageResponse("Recruiter registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(recruiterAuthService.login(request));
    }
}