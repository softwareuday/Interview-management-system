//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.dto.round.*;
//import com.ims.fullstack.service.InterviewRoundService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import jakarta.validation.Valid;
//import com.ims.fullstack.security.AuthUtil;
//
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/rounds")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
//public class InterviewRoundController {
//
//    private final InterviewRoundService roundService;
//
//    // 🔐 Recruiter only — create interview round
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PostMapping
//    public ResponseEntity<InterviewRoundResponse> createRound(
//            @Valid @RequestBody InterviewRoundCreateRequest req
//    ) {
//        InterviewRoundResponse saved = roundService.createRound(req);
//        return ResponseEntity.ok(saved);
//    }
//
//    // 🔐 Recruiter only — update round details
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PutMapping("/{id}")
//    public ResponseEntity<InterviewRoundResponse> updateRound(
//            @PathVariable Long id,
//            @Valid @RequestBody InterviewRoundUpdateRequest req
//    ) {
//        InterviewRoundResponse resp = roundService.updateRound(id, req);
//        return ResponseEntity.ok(resp);
//    }
//
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping("/me")
//    public ResponseEntity<List<InterviewRoundResponse>> getMyRounds() {
//        return ResponseEntity.ok(roundService.getMyRounds());
//    }
//
//
//    // 🔐 Recruiter only — change round status
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PutMapping("/{id}/status")
//    public ResponseEntity<InterviewRoundResponse> changeStatus(
//            @PathVariable Long id,
//            @RequestParam String status
//    ) {
//        InterviewRoundResponse resp = roundService.changeStatus(id, status);
//        return ResponseEntity.ok(resp);
//    }
//
//    // 🔐 Recruiter only — delete round
//    @PreAuthorize("hasRole('RECRUITER')")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteRound(@PathVariable Long id) {
//        roundService.deleteRound(id);
//        return ResponseEntity.ok("Interview round deleted");
//    }
//
//    // 👀 Read-only endpoints (can be tightened later)
//
//    @GetMapping("/job/{jobId}")
//    public ResponseEntity<List<InterviewRoundResponse>> getByJob(@PathVariable Long jobId) {
//        return ResponseEntity.ok(roundService.getByJob(jobId));
//    }
//
//    @GetMapping("/candidate/{candidateId}")
//    public ResponseEntity<List<InterviewRoundResponse>> getByCandidate(@PathVariable Long candidateId) {
//        return ResponseEntity.ok(roundService.getByCandidate(candidateId));
//    }
//
////    @GetMapping("/recruiter/{recruiterId}")
////    public ResponseEntity<List<InterviewRoundResponse>> getByRecruiter(@PathVariable Long recruiterId) {
////        return ResponseEntity.ok(roundService.getByRecruiter(recruiterId));
////    }
//}
package com.ims.fullstack.controller;

import com.ims.fullstack.dto.round.*;
import com.ims.fullstack.service.InterviewRoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class InterviewRoundController {
    private final InterviewRoundService roundService;

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<InterviewRoundResponse> createRound(@Valid @RequestBody InterviewRoundCreateRequest req) {
        return ResponseEntity.ok(roundService.createRound(req));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/me")
    public ResponseEntity<List<InterviewRoundResponse>> getMyRounds() {
        return ResponseEntity.ok(roundService.getMyRounds());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{id}")
    public ResponseEntity<InterviewRoundResponse> updateRound(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRoundUpdateRequest req) {
        return ResponseEntity.ok(roundService.updateRound(id, req));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{id}/status")
    public ResponseEntity<InterviewRoundResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(roundService.changeStatus(id, status));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRound(@PathVariable Long id) {
        roundService.deleteRound(id);
        return ResponseEntity.ok("Interview round deleted");
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<InterviewRoundResponse>> getByCandidate(@PathVariable Long candidateId) {
        return ResponseEntity.ok(roundService.getByCandidate(candidateId));
    }
}