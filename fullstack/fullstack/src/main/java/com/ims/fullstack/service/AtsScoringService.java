//package com.ims.fullstack.service;
//
//import com.ims.fullstack.model.AtsScoreResult;
//import com.ims.fullstack.util.TextSimilarityUtil;
//import com.ims.fullstack.model.Candidate;
//import com.ims.fullstack.model.Job;
//import com.ims.fullstack.repository.CandidateRepository;
//import com.ims.fullstack.repository.JobRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AtsScoringService {
//
//    private final CandidateRepository candidateRepository;
//    private final JobRepository jobRepository;
//    private final ResumeParserService resumeParserService;
//
//    public AtsScoreResult scan(Long candidateId, Long jobId) {
//
//        Candidate candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        String resumeText = resumeParserService.extractText(candidate.getResumeUrl());
//        String jobText = resumeParserService.extractText(job.getDescription());
//
//        Set<String> resumeWords = TextSimilarityUtil.extractKeywords(resumeText);
//        Set<String> jobWords = TextSimilarityUtil.extractKeywords(jobText);
//
//        Set<String> matched = resumeWords.stream()
//                .filter(jobWords::contains)
//                .collect(Collectors.toSet());
//
//        Set<String> missing = jobWords.stream()
//                .filter(word -> !resumeWords.contains(word))
//                .collect(Collectors.toSet());
//
//        int score = TextSimilarityUtil.calculateScore(resumeWords, jobWords);
//
//        return AtsScoreResult.builder()
//                .score(score)
//                .matched(matched.stream().toList())
//                .missing(missing.stream().toList())
//                .build();
//    }
//}
package com.ims.fullstack.service;

import com.ims.fullstack.model.AtsScoreResult;
import com.ims.fullstack.util.TextSimilarityUtil;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.model.Job;
import com.ims.fullstack.repository.CandidateRepository;
import com.ims.fullstack.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtsScoringService {
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ResumeParserService resumeParserService;

    public AtsScoreResult scan(Long candidateId, Long jobId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new RuntimeException("Candidate not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        String resumeText = resumeParserService.extractText(candidate.getResumeUrl());
        String jobText = resumeParserService.extractText(job.getDescription());
        Set<String> resumeWords = TextSimilarityUtil.extractKeywords(resumeText);
        Set<String> jobWords = TextSimilarityUtil.extractKeywords(jobText);
        Set<String> matched = resumeWords.stream().filter(jobWords::contains).collect(Collectors.toSet());
        Set<String> missing = jobWords.stream().filter(word -> !resumeWords.contains(word)).collect(Collectors.toSet());
        int score = TextSimilarityUtil.calculateScore(resumeWords, jobWords);
        return AtsScoreResult.builder().score(score).matched(matched.stream().toList()).missing(missing.stream().toList()).build();
    }
}