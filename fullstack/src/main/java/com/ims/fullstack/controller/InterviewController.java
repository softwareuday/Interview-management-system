////package com.ims.fullstack.controller;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.fasterxml.jackson.databind.DeserializationFeature;
////import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
////import com.fasterxml.jackson.databind.SerializationFeature;
////
////import com.ims.fullstack.model.Interview;
////import com.ims.fullstack.service.InterviewService;
////
////import lombok.RequiredArgsConstructor;
////
////import org.springframework.http.MediaType;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/interviews")
////@RequiredArgsConstructor
////@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
////public class InterviewController {
////
////    private final InterviewService interviewService;
////
////    // JSON Mapper supporting Java 8 date/time
////    private final ObjectMapper objectMapper = new ObjectMapper()
////            .registerModule(new JavaTimeModule())
////            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
////            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
////
////    // =============================
////    // CREATE INTERVIEW (Recruiter)
////    // =============================
////    @PreAuthorize("hasRole('RECRUITER')")
////    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    public ResponseEntity<Interview> createInterview(
////            @RequestParam("interview") String interviewJson,
////            @RequestParam(value = "resume", required = false) MultipartFile resumeFile) {
////
////        try {
////            Interview interview = objectMapper.readValue(interviewJson, Interview.class);
////            Interview saved = interviewService.scheduleInterview(interview, resumeFile);
////            return ResponseEntity.ok(saved);
////
////        } catch (Exception e) {
////            return ResponseEntity.badRequest().build();
////        }
////    }
////
////    // =============================
////    // GET INTERVIEWS (Read-only)
////    // =============================
////    @GetMapping("/recruiter/{recruiterId}")
////    public ResponseEntity<List<Interview>> getAllByRecruiter(@PathVariable Long recruiterId) {
////        return ResponseEntity.ok(interviewService.getAllByRecruiter(recruiterId));
////    }
////
////    @GetMapping("/recruiter/{recruiterId}/upcoming")
////    public ResponseEntity<List<Interview>> getUpcomingByRecruiter(@PathVariable Long recruiterId) {
////        return ResponseEntity.ok(interviewService.getUpcomingByRecruiter(recruiterId));
////    }
////
////    @GetMapping("/recruiter/{recruiterId}/past")
////    public ResponseEntity<List<Interview>> getPastByRecruiter(@PathVariable Long recruiterId) {
////        return ResponseEntity.ok(interviewService.getPastByRecruiter(recruiterId));
////    }
////
////    @GetMapping("/candidate/{candidateId}")
////    public ResponseEntity<List<Interview>> getAllByCandidate(@PathVariable Long candidateId) {
////        return ResponseEntity.ok(interviewService.getAllByCandidate(candidateId));
////    }
////
////    // =============================
////    // UPDATE / CANCEL (Recruiter)
////    // =============================
////    @PreAuthorize("hasRole('RECRUITER')")
////    @PutMapping("/{id}")
////    public ResponseEntity<Interview> updateInterview(
////            @PathVariable Long id,
////            @RequestBody Interview interview) {
////        return ResponseEntity.ok(interviewService.updateInterview(id, interview));
////    }
////
////    @PreAuthorize("hasRole('RECRUITER')")
////    @PutMapping("/{id}/status")
////    public ResponseEntity<Interview> updateStatus(
////            @PathVariable Long id,
////            @RequestParam String status) {
////        return ResponseEntity.ok(interviewService.updateStatus(id, status));
////    }
////
////    @PreAuthorize("hasRole('RECRUITER')")
////    @DeleteMapping("/{id}/cancel")
////    public ResponseEntity<String> cancelInterview(@PathVariable Long id) {
////        interviewService.cancelInterview(id);
////        return ResponseEntity.ok("Interview cancelled successfully.");
////    }
////}
//
//package com.ims.fullstack.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.ims.fullstack.model.Interview;
//import com.ims.fullstack.security.AuthUtil;
//import com.ims.fullstack.service.InterviewService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/interviews")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
//public class InterviewController {
//
//    private final InterviewService interviewService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper()
//            .registerModule(new JavaTimeModule())
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//    // =============================
//    // CREATE INTERVIEW (Recruiter)
//    // =============================
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Interview> createInterview(
//            @RequestParam("interview") String interviewJson,
//            @RequestParam(value = "resume", required = false) MultipartFile resumeFile) {
//
//        try {
//            Interview interview = objectMapper.readValue(interviewJson, Interview.class);
//
//            // 🔐 enforce recruiter identity from JWT
//            interview.setRecruiterId(AuthUtil.getUserId());
//
//            return ResponseEntity.ok(
//                    interviewService.scheduleInterview(interview, resumeFile)
//            );
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // =============================
//    // 🔐 Recruiter – ALL interviews (dashboard)
//    // MATCHES: GET /api/interviews
//    // =============================
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping
//    public ResponseEntity<List<Interview>> getMyInterviews() {
//        return ResponseEntity.ok(
//                interviewService.getMyInterviews()
//        );
//    }
//
//
//    // =============================
//    // Candidate interviews
//    // =============================
//    @GetMapping("/candidate/{candidateId}")
//    public ResponseEntity<List<Interview>> getAllByCandidate(@PathVariable Long candidateId) {
//        return ResponseEntity.ok(interviewService.getAllByCandidate(candidateId));
//    }
//
//    // =============================
//    // UPDATE / CANCEL (Recruiter)
//    // =============================
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PutMapping("/{id}")
//    public ResponseEntity<Interview> updateInterview(
//            @PathVariable Long id,
//            @RequestBody Interview interview) {
//
//        return ResponseEntity.ok(
//                interviewService.updateInterview(id, interview)
//        );
//    }
//
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Interview> updateStatus(
//            @PathVariable Long id,
//            @RequestParam String status) {
//
//        return ResponseEntity.ok(
//                interviewService.updateStatus(id, status)
//        );
//    }
//
//    @PreAuthorize("hasRole('RECRUITER')")
//    @DeleteMapping("/{id}/cancel")
//    public ResponseEntity<String> cancelInterview(@PathVariable Long id) {
//        interviewService.cancelInterview(id);
//        return ResponseEntity.ok("Interview cancelled successfully.");
//    }
//}




package com.ims.fullstack.controller;

import com.ims.fullstack.dto.round.InterviewRequest;
import com.ims.fullstack.dto.round.InterviewResponse;
import com.ims.fullstack.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<InterviewResponse> scheduleInterview(@RequestBody InterviewRequest request) {
        return ResponseEntity.ok(interviewService.scheduleInterview(request));
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<InterviewResponse>> getRecruiterInterviews() {
        return ResponseEntity.ok(interviewService.getRecruiterInterviews());
    }

    @GetMapping("/candidate")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<InterviewResponse>> getCandidateInterviews() {
        return ResponseEntity.ok(interviewService.getCandidateInterviews());
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> cancelInterview(@PathVariable Long id) {
        interviewService.cancelInterview(id);
        return ResponseEntity.ok().build();
    }
}