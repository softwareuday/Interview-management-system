////package com.ims.fullstack.controller;
////
////import com.ims.fullstack.dto.recruiter.JobRequest;
////import com.ims.fullstack.dto.recruiter.JobResponse;
////import com.ims.fullstack.model.Job;
////import com.ims.fullstack.service.JobService;
////import lombok.RequiredArgsConstructor;
////import org.springframework.data.domain.Page;
////import org.springframework.data.domain.PageRequest;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////import java.util.stream.Collectors;
////
////@RestController
////@RequestMapping("/api/recruiters")
////@RequiredArgsConstructor
////public class JobController {
////
////    private final JobService jobService;
////
////    @PostMapping("/{recruiterId}/jobs")
////    @PreAuthorize("hasRole('RECRUITER') or true")
////    public ResponseEntity<?> createJob(
////            @PathVariable Long recruiterId,
////            @RequestBody JobRequest jobRequest) {
////
////        System.out.println("POST /api/recruiters/" + recruiterId + "/jobs");
////        Job job = jobService.createJob(jobRequest, recruiterId);
////        System.out.println("Created job id=" + job.getId());
////        return ResponseEntity.status(HttpStatus.CREATED)
////                .body(JobResponse.fromEntity(job));
////    }
////
////    @GetMapping("/{recruiterId}/jobs")
////    @PreAuthorize("hasRole('RECRUITER') or true")
////    public ResponseEntity<?> getJobsByRecruiter(
////            @PathVariable Long recruiterId,
////            @RequestParam(defaultValue = "0") int page,
////            @RequestParam(defaultValue = "10") int size) {
////
////        System.out.println("GET /api/recruiters/" + recruiterId + "/jobs page=" + page + " size=" + size);
////        Page<Job> jobs = jobService.listJobsByRecruiter(recruiterId, PageRequest.of(page, size));
////
////        List<JobResponse> response = jobs.getContent().stream()
////                .map(JobResponse::fromEntity)
////                .collect(Collectors.toList());
////
////        System.out.println("Returning " + response.size() + " jobs");
////        return ResponseEntity.ok(response);
////    }
////
////    @GetMapping("/jobs/{id}")
////    @PreAuthorize("hasRole('RECRUITER') or true")
////    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
////        System.out.println("GET /api/recruiters/jobs/" + id);
////        Job job = jobService.getJob(id);
////        return ResponseEntity.ok(JobResponse.fromEntity(job));
////    }
////
////    @PutMapping("/jobs/{id}")
////    @PreAuthorize("hasRole('RECRUITER') or true")
////    public ResponseEntity<?> updateJob(
////            @PathVariable Long id,
////            @RequestBody JobRequest jobRequest) {
////
////        System.out.println("PUT /api/recruiters/jobs/" + id);
////        Job updated = jobService.updateJob(id, jobRequest);
////        return ResponseEntity.ok(JobResponse.fromEntity(updated));
////    }
////
////    @PutMapping("/jobs/{id}/close")
////    @PreAuthorize("hasRole('RECRUITER') or true")
////    public ResponseEntity<?> closeJob(@PathVariable Long id) {
////        System.out.println("PUT /api/recruiters/jobs/" + id + "/close");
////        jobService.closeJob(id);
////        return ResponseEntity.ok("Job closed successfully");
////    }
////}
//
//
////
////package com.ims.fullstack.controller;
////
////import com.ims.fullstack.dto.application.ApplicationResponse;
////import com.ims.fullstack.dto.recruiter.JobRequest;
////import com.ims.fullstack.dto.recruiter.JobResponse;
////import com.ims.fullstack.service.ApplicationService;
////import com.ims.fullstack.service.JobService;
////import jakarta.servlet.http.HttpServletRequest;
////import lombok.RequiredArgsConstructor;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@RestController
////@RequiredArgsConstructor
////public class JobController {
////
////    private final JobService jobService;
////    private final ApplicationService applicationService;
////
////    // 🟢 Manage Jobs page
////    @GetMapping("/api/jobs")
////    public List<JobResponse> myJobs(HttpServletRequest request) {
////        return jobService.getMyJobs(request);
////    }
////
////    // 🟢 Create Job
////    @PostMapping("/api/recruiter/jobs")
////    public JobResponse createJob(
////            @RequestBody JobRequest jobRequest,
////            HttpServletRequest request
////    ) {
////        return jobService.createJob(jobRequest, request);
////    }
////
////    // 🟢 Close Job
////    @PutMapping("/api/recruiter/jobs/{jobId}/close")
////    public void closeJob(@PathVariable Long jobId) {
////        jobService.closeJob(jobId);
////    }
////    @PreAuthorize("hasRole('RECRUITER')")
////    @GetMapping("/api/jobs/{jobId}/applications")
////    public List<ApplicationResponse> getJobApplications(
////            @PathVariable Long jobId,
////            HttpServletRequest request
////    ) {
////        return applicationService.getApplicantsForRecruiter(jobId, request);
////    }
////
////}
//
////
////package com.ims.fullstack.controller;
////
////import com.ims.fullstack.dto.application.ApplicationResponse;
////import com.ims.fullstack.dto.recruiter.JobRequest;
////import com.ims.fullstack.dto.recruiter.JobResponse;
////import com.ims.fullstack.service.ApplicationService;
////import com.ims.fullstack.service.JobService;
////import jakarta.servlet.http.HttpServletRequest;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////import java.util.Map;
////
////@RestController
////@RequestMapping("/api")
////@RequiredArgsConstructor
////public class JobController {
////
////    private final JobService jobService;
////    private final ApplicationService applicationService;
////
////    // ========== JOB ENDPOINTS ==========
////
////    // 1. GET ALL JOBS FOR RECRUITER
////    @GetMapping("/jobs")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public List<JobResponse> getRecruiterJobs(HttpServletRequest request) {
////        return jobService.getMyJobs(request);
////    }
////
////    // 2. GET SINGLE JOB DETAILS
////    @GetMapping("/jobs/{jobId}")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public JobResponse getJobDetails(@PathVariable Long jobId, HttpServletRequest request) {
////        return jobService.getJobById(jobId, request);
////    }
////
////    // 3. CREATE NEW JOB (your existing endpoint)
////    @PostMapping("/recruiter/jobs")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public JobResponse createJob(
////            @RequestBody JobRequest jobRequest,
////            HttpServletRequest request
////    ) {
////        return jobService.createJob(jobRequest, request);
////    }
////
////    // 4. UPDATE JOB
////    @PutMapping("/recruiter/jobs/{jobId}")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public JobResponse updateJob(
////            @PathVariable Long jobId,
////            @RequestBody JobRequest jobRequest,
////            HttpServletRequest request
////    ) {
////        return jobService.updateJob(jobId, jobRequest, request);
////    }
////
////    // 5. DELETE JOB
////    @DeleteMapping("/recruiter/jobs/{jobId}")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public ResponseEntity<Map<String, String>> deleteJob(@PathVariable Long jobId) {
////        jobService.deleteJob(jobId);
////        return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
////    }
////
////    // 6. CLOSE JOB (your existing endpoint)
////    @PutMapping("/recruiter/jobs/{jobId}/close")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public ResponseEntity<Map<String, String>> closeJob(@PathVariable Long jobId) {
////        jobService.closeJob(jobId);
////        return ResponseEntity.ok(Map.of("message", "Job closed successfully"));
////    }
////
////    // 7. REOPEN JOB (if needed)
////    @PutMapping("/recruiter/jobs/{jobId}/reopen")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public ResponseEntity<Map<String, String>> reopenJob(@PathVariable Long jobId) {
////        jobService.reopenJob(jobId);
////        return ResponseEntity.ok(Map.of("message", "Job reopened successfully"));
////    }
////
////    // ========== APPLICATION ENDPOINTS ==========
////
////    // 8. GET APPLICATIONS FOR A JOB (your existing endpoint)
////    @GetMapping("/jobs/{jobId}/applications")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public List<ApplicationResponse> getJobApplications(
////            @PathVariable Long jobId,
////            HttpServletRequest request
////    ) {
////        return applicationService.getApplicantsForRecruiter(jobId, request);
////    }
////
////    // 9. UPDATE APPLICATION STATUS
////    @PutMapping("/applications/{applicationId}/status")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public ResponseEntity<Map<String, String>> updateApplicationStatus(
////            @PathVariable Long applicationId,
////            @RequestBody Map<String, String> statusUpdate
////    ) {
////        String status = statusUpdate.get("status");
////        applicationService.updateApplicationStatus(applicationId, status);
////        return ResponseEntity.ok(Map.of("message", "Application status updated to " + status));
////    }
////
////    // 10. GET ALL APPLICATIONS (for recruiter dashboard)
////    @GetMapping("/recruiter/applications")
////    @PreAuthorize("hasRole('RECRUITER')")
////    public List<ApplicationResponse> getAllApplications(HttpServletRequest request) {
////        return applicationService.getAllApplicationsForRecruiter(request);
////    }
////}
//
//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.dto.application.ApplicationResponse;
//import com.ims.fullstack.dto.recruiter.JobRequest;
//import com.ims.fullstack.dto.recruiter.JobResponse;
//import com.ims.fullstack.service.ApplicationService;
//import com.ims.fullstack.service.JobService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class JobController {
//
//    private final JobService jobService;
//    private final ApplicationService applicationService;
//
//    // 🟢 GET: Get all jobs for recruiter (existing)
//    @GetMapping("/api/jobs")
//    public List<JobResponse> myJobs(HttpServletRequest request) {
//        return jobService.getMyJobs(request);
//    }
//
//    // 🟢 POST: Create job (existing - matches your backend)
//    @PostMapping("/api/recruiter/jobs")
//    public JobResponse createJob(
//            @RequestBody JobRequest jobRequest,
//            HttpServletRequest request
//    ) {
//        return jobService.createJob(jobRequest, request);
//    }
//
//    // 🟢 PUT: Close job (existing)
//    @PutMapping("/api/recruiter/jobs/{jobId}/close")
//    public void closeJob(@PathVariable Long jobId) {
//        jobService.closeJob(jobId);
//    }
//
//    // 🟢 GET: Get applications for a job (existing)
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping("/api/jobs/{jobId}/applications")
//    public List<ApplicationResponse> getJobApplications(
//            @PathVariable Long jobId,
//            HttpServletRequest request
//    ) {
//        return applicationService.getApplicantsForRecruiter(jobId, request);
//    }
//}

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