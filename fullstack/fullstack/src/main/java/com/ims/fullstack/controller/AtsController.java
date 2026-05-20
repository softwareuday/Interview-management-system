package com.ims.fullstack.controller;

import com.ims.fullstack.dto.ats.AtsScanRequest;
import com.ims.fullstack.dto.ats.AtsScanResponse;
import com.ims.fullstack.model.AtsScoreResult;
import com.ims.fullstack.model.Application;
import com.ims.fullstack.security.AuthUtil;
import com.ims.fullstack.service.ApplicationService;
import com.ims.fullstack.service.AtsScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ats")
@RequiredArgsConstructor
public class AtsController {

    private final AtsScoringService atsScoringService;
    private final ApplicationService applicationService;

    @PostMapping("/scan")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<AtsScanResponse> scan(@RequestBody AtsScanRequest request) {
        Long candidateId = AuthUtil.getUserId();
        if (candidateId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You must be logged in to scan your resume. Please create a profile and upload a resume.");
        }

        AtsScoreResult result = atsScoringService.scan(candidateId, request.getJobId());

        return ResponseEntity.ok(AtsScanResponse.builder()
                .atsScore(result.getScore())
                .matchedKeywords(result.getMatched())
                .missingKeywords(result.getMissing())
                .recommendation(result.getRecommendations())
                .build());
    }

    @PostMapping("/scan-application/{applicationId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<AtsScanResponse> scanApplication(@PathVariable Long applicationId) {
        Long recruiterId = AuthUtil.getUserId();
        Application application = applicationService.getApplicationById(applicationId);

        if (!application.getRecruiter().getId().equals(recruiterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to access this application");
        }

        AtsScoreResult result = atsScoringService.scan(
                application.getCandidate().getId(),
                application.getJob().getId()
        );

        // Store the score in the application
        application.setAtsScore(result.getScore());
        application.setMatchedKeywords(String.join(",", result.getMatched()));
        application.setMissingKeywords(String.join(",", result.getMissing()));
        applicationService.save(application);

        return ResponseEntity.ok(AtsScanResponse.builder()
                .atsScore(result.getScore())
                .matchedKeywords(result.getMatched())
                .missingKeywords(result.getMissing())
                .recommendation(result.getRecommendations())
                .build());
    }
}