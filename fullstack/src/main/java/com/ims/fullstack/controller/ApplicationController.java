package com.ims.fullstack.controller;

import com.ims.fullstack.dto.application.ApplyRequest;
import com.ims.fullstack.dto.application.ApplicationResponse;
import com.ims.fullstack.dto.application.UpdateStatusRequest;
import com.ims.fullstack.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> apply(
            @RequestPart("request") ApplyRequest request,
            @RequestPart(value = "resume", required = false) MultipartFile resume) {
        // No @PreAuthorize - open to both authenticated and guest users
        return ResponseEntity.ok(applicationService.applyForJob(request, resume));
    }

    @GetMapping("/candidate")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationResponse>> getCandidateApplications() {
        return ResponseEntity.ok(applicationService.getCandidateApplications());
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<ApplicationResponse>> getRecruiterApplications() {
        return ResponseEntity.ok(applicationService.getRecruiterApplications());
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<ApplicationResponse>> getJobApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getJobApplications(jobId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest statusRequest) {
        applicationService.updateStatus(id, statusRequest.getStatus());
        return ResponseEntity.ok().build();
    }
}