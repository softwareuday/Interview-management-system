////package com.ims.fullstack.controller;
////
////import com.ims.fullstack.dto.application.ApplyRequest;
////import com.ims.fullstack.dto.application.UpdateStatusRequest;
////import com.ims.fullstack.model.Application;
////import com.ims.fullstack.service.ApplicationService;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/applications")
////@RequiredArgsConstructor
////@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
////public class ApplicationController {
////
////    private final ApplicationService service;
////
////    @PostMapping(value = "/apply", consumes = "multipart/form-data")
////    public ResponseEntity<Application> apply(
////            @RequestPart("data") ApplyRequest req,
////            @RequestPart(value = "resume", required = false) MultipartFile resume
////    ) {
////        System.out.println("POST /api/applications/apply by candidateId=" + req.getCandidateId() + " jobId=" + req.getJobId());
////        if (resume != null) System.out.println("Resume attached: " + resume.getOriginalFilename() + " size=" + resume.getSize());
////        Application saved = service.applyForJob(req, resume);
////        System.out.println("Application saved id=" + saved.getId());
////        return ResponseEntity.ok(saved);
////    }
////
////    @GetMapping("/recruiter/{recruiterId}")
////    public ResponseEntity<List<Application>> recruiterApps(@PathVariable Long recruiterId) {
////        System.out.println("GET /api/applications/recruiter/" + recruiterId);
////        return ResponseEntity.ok(service.getByRecruiter(recruiterId));
////    }
////
////    @GetMapping("/candidate/{candidateId}")
////    public ResponseEntity<List<Application>> candidateApps(@PathVariable Long candidateId) {
////        System.out.println("GET /api/applications/candidate/" + candidateId);
////        return ResponseEntity.ok(service.getByCandidate(candidateId));
////    }
////
////    @GetMapping("/job/{jobId}")
////    public ResponseEntity<List<Application>> jobApplicants(@PathVariable Long jobId) {
////        System.out.println("GET /api/applications/job/" + jobId);
////        return ResponseEntity.ok(service.getApplicants(jobId));
////    }
////
////    @PutMapping("/{id}/status")
////    public ResponseEntity<Application> updateStatus(
////            @PathVariable Long id,
////            @RequestBody UpdateStatusRequest req
////    ) {
////        System.out.println("PUT /api/applications/" + id + "/status -> " + req.getStatus());
////        return ResponseEntity.ok(service.updateStatus(id, req));
////    }
////}
//
//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.dto.application.ApplyRequest;
//import com.ims.fullstack.dto.application.ApplicationResponse;
//import com.ims.fullstack.service.ApplicationService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/applications")
//@RequiredArgsConstructor
//public class ApplicationController {
//
//    private final ApplicationService applicationService;
//
//    // ===============================
//    // CANDIDATE APPLY FOR JOB
//    // ===============================
//    @PreAuthorize("hasRole('CANDIDATE')")
//    @PostMapping(value = "/apply", consumes = "multipart/form-data")
//    public ResponseEntity<ApplicationResponse> applyForJob(
//            @RequestPart("data") ApplyRequest request,
//            @RequestPart(value = "resume", required = false) MultipartFile resume,
//            HttpServletRequest httpRequest
//    ) {
//        return ResponseEntity.ok(
//                applicationService.applyForJob(request, resume, httpRequest)
//        );
//    }
//
//    // ===============================
//    // RECRUITER VIEW APPLICANTS
//    // ===============================
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping("/recruiter/jobs/{jobId}")
//    public ResponseEntity<List<ApplicationResponse>> viewApplicants(
//            @PathVariable Long jobId,
//            HttpServletRequest request
//    ) {
//        return ResponseEntity.ok(
//                applicationService.getApplicantsForRecruiter(jobId, request)
//        );
//    }
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PutMapping("/{applicationId}/status")
//    public ResponseEntity<Void> updateApplicationStatus(
//            @PathVariable Long applicationId,
//            @RequestParam String status,
//            HttpServletRequest request
//    ) {
//        applicationService.updateStatus(applicationId, status, request);
//        return ResponseEntity.ok().build();
//    }
//
//
//
//    // ===============================
//// RECRUITER VIEW APPLICANTS (FRONTEND-COMPATIBLE)
//// ===============================
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping("/jobs/{jobId}/applications")
//    public ResponseEntity<List<ApplicationResponse>> getJobApplications(
//            @PathVariable Long jobId,
//            HttpServletRequest request
//    ) {
//        return ResponseEntity.ok(
//                applicationService.getApplicantsForRecruiter(jobId, request)
//        );
//    }
//
//}



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
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> apply(@RequestBody ApplyRequest request) {
        return ResponseEntity.ok(applicationService.applyForJob(request, null));
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