package com.ims.fullstack.service;

import com.ims.fullstack.model.AtsScoreResult;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.model.Job;
import com.ims.fullstack.repository.CandidateRepository;
import com.ims.fullstack.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AtsScoringService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ResumeParserService resumeParserService;
    private final GeminiAtsService geminiAtsService;

    public AtsScoreResult scan(Long candidateId, Long jobId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

        if (candidate.getResumeUrl() == null || candidate.getResumeUrl().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Please upload your resume in your profile before using the ATS scanner.");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        String resumeText = resumeParserService.extractTextFromFile(candidate.getResumeUrl());
        String jobDescription = job.getDescription() != null ? job.getDescription() : "";

        Map<String, Object> aiResult = geminiAtsService.analyzeResume(resumeText, jobDescription);

        int score = (int) aiResult.get("score");
        List<String> matched = (List<String>) aiResult.get("matchedSkills");
        List<String> missing = (List<String>) aiResult.get("missingSkills");
        String recommendation = (String) aiResult.get("recommendation");

        return AtsScoreResult.builder()
                .score(score)
                .matched(matched)
                .missing(missing)
                .recommendations(recommendation)
                .build();
    }
}