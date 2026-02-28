package com.ims.fullstack.controller;

import com.ims.fullstack.dto.candidate.CandidateProfileResponse;
import com.ims.fullstack.dto.candidate.CandidateProfileUpdateRequest;
import com.ims.fullstack.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate/profile")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService profileService;

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> updateProfile(@RequestBody CandidateProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }
}