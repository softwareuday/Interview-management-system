//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.model.Candidate;
//import com.ims.fullstack.repository.CandidateRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/recruiter")
//@RequiredArgsConstructor
//public class CandidateController {
//
//    private final CandidateRepository candidateRepository;
//
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping("/candidates/{candidateId}")
//    public Candidate viewCandidateProfile(@PathVariable Long candidateId) {
//        return candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//    }
//}
