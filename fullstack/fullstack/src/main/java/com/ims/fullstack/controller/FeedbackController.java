//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.service.FeedbackService;
//import com.ims.fullstack.dto.feedback.FeedbackRequest;
//import com.ims.fullstack.dto.feedback.FeedbackResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/feedback")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
//public class FeedbackController {
//
//    private final FeedbackService feedbackService;
//
//    // 🔐 Recruiter only — submit feedback
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PostMapping("/{roundId}")
//    public ResponseEntity<FeedbackResponse> submitFeedback(
//            @PathVariable Long roundId,
//            @Valid @RequestBody FeedbackRequest request
//    ) {
//        FeedbackResponse resp = feedbackService.submitFeedback(roundId, request);
//        return ResponseEntity.ok(resp);
//    }
//
//    // 🔐 Recruiter only — update feedback
//    @PreAuthorize("hasRole('RECRUITER')")
//    @PutMapping("/{roundId}")
//    public ResponseEntity<FeedbackResponse> updateFeedback(
//            @PathVariable Long roundId,
//            @Valid @RequestBody FeedbackRequest request
//    ) {
//        FeedbackResponse resp = feedbackService.updateFeedback(roundId, request);
//        return ResponseEntity.ok(resp);
//    }
//
//    // 👀 Read-only endpoints
//
//    @GetMapping("/round/{roundId}")
//    public ResponseEntity<FeedbackResponse> getByRound(@PathVariable Long roundId) {
//        return ResponseEntity.ok(feedbackService.getByRound(roundId));
//    }
//
//    @GetMapping("/recruiter/{recruiterId}")
//    public ResponseEntity<List<FeedbackResponse>> getByRecruiter(@PathVariable Long recruiterId) {
//        return ResponseEntity.ok(feedbackService.getByRecruiter(recruiterId));
//    }
//
//    @GetMapping("/candidate/{candidateId}")
//    public ResponseEntity<List<FeedbackResponse>> getByCandidate(@PathVariable Long candidateId) {
//        return ResponseEntity.ok(feedbackService.getByCandidate(candidateId));
//    }
//}

package com.ims.fullstack.controller;

import com.ims.fullstack.dto.feedback.FeedbackRequest;
import com.ims.fullstack.dto.feedback.FeedbackResponse;
import com.ims.fullstack.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/{roundId}")
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @PathVariable Long roundId,
            @Valid @RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.submitFeedback(roundId, request));
    }

    @GetMapping("/round/{roundId}")
    public ResponseEntity<FeedbackResponse> getByRound(@PathVariable Long roundId) {
        return ResponseEntity.ok(feedbackService.getByRound(roundId));
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<FeedbackResponse>> getByRecruiter(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(feedbackService.getByRecruiter(recruiterId));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<FeedbackResponse>> getByCandidate(@PathVariable Long candidateId) {
        return ResponseEntity.ok(feedbackService.getByCandidate(candidateId));
    }
}