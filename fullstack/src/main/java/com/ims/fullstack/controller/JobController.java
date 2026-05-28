

package com.ims.fullstack.controller;

import com.ims.fullstack.dto.recruiter.JobRequest;
import com.ims.fullstack.dto.recruiter.JobResponse;
import com.ims.fullstack.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // =============================
    // Get recruiter's jobs (matches frontend /recruiter/jobs)
    // =============================
    @GetMapping("/api/recruiter/jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobResponse>> getMyJobs() {
        return ResponseEntity.ok(jobService.getMyJobs());
    }

    // =============================
    // Create a new job
    // =============================
    @PostMapping("/api/recruiter/jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.createJob(request));
    }

    // =============================
    // Get job by ID (for editing)
    // =============================
    @GetMapping("/api/recruiter/jobs/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        // Passing null for candidateId because it's a recruiter call
        return ResponseEntity.ok(jobService.getJobById(id, null));
    }

    // =============================
    // Update job
    // =============================
    @PutMapping("/api/recruiter/jobs/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(id, request));
    }

    // =============================
    // Close job (PATCH to match frontend)
    // =============================
    @PatchMapping("/api/recruiter/jobs/{id}/close")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> closeJob(@PathVariable Long id) {
        jobService.closeJob(id);
        return ResponseEntity.ok().build();
    }

    // =============================
    // Delete job
    // =============================
    @DeleteMapping("/api/recruiter/jobs/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }
}