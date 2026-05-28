
package com.ims.fullstack.controller;

import com.ims.fullstack.dto.AuthResponse;
import com.ims.fullstack.dto.LoginRequest;
import com.ims.fullstack.dto.candidate.CandidateRegisterRequest;
import com.ims.fullstack.service.CandidateAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/candidate")
@RequiredArgsConstructor
public class CandidateAuthController {
    private final CandidateAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody CandidateRegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Candidate registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}