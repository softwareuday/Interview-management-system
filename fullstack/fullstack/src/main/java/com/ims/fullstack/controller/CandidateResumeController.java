////package com.ims.fullstack.controller;
////
////import com.ims.fullstack.dto.candidate.ResumeUploadResponse;
////import com.ims.fullstack.security.JwtService;
////import com.ims.fullstack.service.CandidateResumeService;
////import jakarta.servlet.http.HttpServletRequest;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.MediaType;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////@RestController
////@RequestMapping("/api/candidates")
////@RequiredArgsConstructor
////public class CandidateResumeController {
////
////    private final CandidateResumeService resumeService;
////    private final JwtService jwtService;
////
////    @PostMapping(
////            value = "/resume",
////            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
////    )
////    public ResponseEntity<ResumeUploadResponse> uploadResume(
////            @RequestParam("resume") MultipartFile resume,
////            HttpServletRequest request
////    ) {
////
////        String authHeader = request.getHeader("Authorization");
////        String token = authHeader.substring(7); // remove "Bearer "
////        Long candidateId = jwtService.extractId(token);
////
////        ResumeUploadResponse response =
////                resumeService.uploadResume(candidateId, resume);
////
////        return ResponseEntity.ok(response);
////    }
////}
//
//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.dto.candidate.ResumeUploadResponse;
//import com.ims.fullstack.model.Candidate;
//import com.ims.fullstack.repository.CandidateRepository;
//import com.ims.fullstack.security.JwtService;
//import com.ims.fullstack.service.CandidateResumeService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/candidates")
//@RequiredArgsConstructor
//public class CandidateResumeController {
//
//    private final CandidateResumeService resumeService;
//    private final JwtService jwtService;
//    private final CandidateRepository candidateRepository;
//
//    // 🔐 Only CANDIDATE can upload resume
//    @PreAuthorize("hasRole('CANDIDATE')")
//    @PostMapping(
//            value = "/resume",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<ResumeUploadResponse> uploadResume(
//            @RequestParam("resume") MultipartFile resume,
//            HttpServletRequest request
//    ) {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Missing or invalid Authorization header");
//        }
//
//        String token = authHeader.substring(7); // remove "Bearer "
//        String email = jwtService.extractUsername(token);
//
//        Candidate candidate = candidateRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//
//        ResumeUploadResponse response =
//                resumeService.uploadResume(candidate.getId(), resume);
//
//        return ResponseEntity.ok(response);
//    }
//}

package com.ims.fullstack.controller;

import com.ims.fullstack.dto.candidate.ResumeUploadResponse;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.repository.CandidateRepository;
import com.ims.fullstack.security.JwtService;
import com.ims.fullstack.service.CandidateResumeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/candidate/resume")
@RequiredArgsConstructor
public class CandidateResumeController {
    private final CandidateResumeService resumeService;
    private final JwtService jwtService;
    private final CandidateRepository candidateRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ResumeUploadResponse> uploadResume(
            @RequestParam("file") MultipartFile file,   // Changed from "resume" to "file"
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        ResumeUploadResponse response = resumeService.uploadResume(candidate.getId(), file);
        return ResponseEntity.ok(response);
    }
}