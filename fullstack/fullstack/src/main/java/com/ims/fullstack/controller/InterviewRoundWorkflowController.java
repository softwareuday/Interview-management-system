//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.dto.round.InterviewRoundCreateRequest;
//import com.ims.fullstack.dto.round.InterviewRoundResponse;
//import com.ims.fullstack.service.RoundWorkflowService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/api/rounds")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
//public class InterviewRoundWorkflowController {
//
//    private final RoundWorkflowService workflowService;
//
//    /**
//     * Complete the current round. Optionally pass a JSON body describing the next round to auto-create it.
//     *
//     * Example:
//     * POST /api/rounds/{id}/complete
//     * Body (optional): { "roundName": "HR Round", "interviewTime": "2025-12-20T10:00:00", "meetingLink":"..." }
//     */
//    @PostMapping("/{id}/complete")
//    public ResponseEntity<?> completeRound(
//            @PathVariable Long id,
//            @RequestBody(required = false) InterviewRoundCreateRequest nextRoundTemplate
//    ) {
//        InterviewRoundResponse next = workflowService.completeRound(id, nextRoundTemplate);
//        if (next != null) return ResponseEntity.ok(next);
//        return ResponseEntity.ok("Round completed (no next round auto-created).");
//    }
//
//    /**
//     * Manual create next round from template (recruiter-driven).
//     */
//    @PostMapping("/{id}/next")
//    public ResponseEntity<InterviewRoundResponse> createNext(
//            @PathVariable Long id,
//            @Valid @RequestBody InterviewRoundCreateRequest template) {
//
//        InterviewRoundResponse created = workflowService.manualCreateNext(id, template);
//        return ResponseEntity.ok(created);
//    }
//}
