//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.feedback.FeedbackRequest;
//import com.ims.fullstack.dto.feedback.FeedbackResponse;
//import com.ims.fullstack.model.*;
//import com.ims.fullstack.model.enums.CandidateStage;
//import com.ims.fullstack.model.enums.FeedbackResult;
//import com.ims.fullstack.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class FeedbackService {
//
//    private final FeedbackRepository feedbackRepository;
//    private final InterviewRoundRepository roundRepository;
//    private final RecruiterRepository recruiterRepository;
//    private final CandidateJobRepository candidateJobRepository;
//    private final MailService mailService;
//
//    /**
//     * Submit feedback for a round. If feedback already exists -> throw or update based on policy (we will upsert: create if absent, update if present).
//     * Will set interviewRound.feedbackSubmitted = true and update candidate stage.
//     */
//    @Transactional
//    public FeedbackResponse submitFeedback(Long roundId, FeedbackRequest req) {
//        InterviewRound round = roundRepository.findById(roundId)
//                .orElseThrow(() -> new RuntimeException("InterviewRound not found: " + roundId));
//
//        // Only allow feedback once round is completed (industry standard: can still accept feedback if not completed,
//        // but we follow: prefer to submit feedback after completion; however we won't block — but we will prefer to check)
//        // If you want to enforce, uncomment:
//        // if (round.getStatus() != InterviewStatus.COMPLETED) throw new RuntimeException("Round must be completed before feedback.");
//
//        // Determine recruiter who submitted feedback
//        Recruiter submittedBy = resolveRecruiter(req.getSubmittedById());
//
//        // Upsert: if feedback exists for this round, update; else create new
//        Optional<Feedback> existingOpt = feedbackRepository.findByInterviewRound_Id(roundId);
//        Feedback f;
//        if (existingOpt.isPresent()) {
//            f = existingOpt.get();
//            f.setTechnicalRating(req.getTechnicalRating());
//            f.setCommunicationRating(req.getCommunicationRating());
//            f.setComments(req.getComments());
//            f.setResult(req.getResult());
//            f.setSubmittedAt(LocalDateTime.now());
//            f.setSubmittedBy(submittedBy);
//        } else {
//            f = Feedback.builder()
//                    .technicalRating(req.getTechnicalRating())
//                    .communicationRating(req.getCommunicationRating())
//                    .comments(req.getComments())
//                    .result(req.getResult())
//                    .submittedAt(LocalDateTime.now())
//                    .submittedBy(submittedBy)
//                    .interviewRound(round)
//                    .build();
//        }
//
//        Feedback saved = feedbackRepository.save(f);
//
//        // link feedback to round and mark submitted
//        round.setFeedback(saved);
//        round.setFeedbackSubmitted(true);
//        roundRepository.save(round);
//
//        // Update candidate stage based on feedback result (industry mapping)
//        updateCandidateStageAfterFeedback(round.getCandidate(), round.getJob(), req.getResult());
//
//        // Send a simple status email to the candidate (not comments)
//        try {
//            Candidate c = round.getCandidate();
//            if (c != null && c.getEmail() != null) {
//                String subj = "Interview Update - " + req.getResult();
//                String body;
//                if (req.getResult() == FeedbackResult.SELECTED) {
//                    body = "Hi " + (c.getFullName() != null ? c.getFullName() : c.getEmail())
//                            + ",\n\nGood news — you have been selected for the position '" + (round.getJob()!=null ? round.getJob().getTitle() : "") + "'. HR will follow up.\n\nRegards,\nRecruitment Team";
//                } else if (req.getResult() == FeedbackResult.REJECTED) {
//                    body = "Hi " + (c.getFullName() != null ? c.getFullName() : c.getEmail())
//                            + ",\n\nThank you for your time. After evaluation, we will not be moving forward with your application for '" + (round.getJob()!=null ? round.getJob().getTitle() : "") + "'.\n\nRegards,\nRecruitment Team";
//                } else { // HOLD
//                    body = "Hi " + (c.getFullName() != null ? c.getFullName() : c.getEmail())
//                            + ",\n\nYour application is on hold for '" + (round.getJob()!=null ? round.getJob().getTitle() : "") + "'. We will update you soon.\n\nRegards,\nRecruitment Team";
//                }
//
//                mailService.sendInterviewInvite(c.getEmail(), subj, body, round);
//            }
//        } catch (Exception e) {
//            // don't fail on mail
//        }
//
//        return toResponse(saved);
//    }
//
//    @Transactional
//    public FeedbackResponse updateFeedback(Long roundId, FeedbackRequest req) {
//        // For update, reuse submitFeedback (which upserts)
//        return submitFeedback(roundId, req);
//    }
//
//    public FeedbackResponse getByRound(Long roundId) {
//        Feedback fb = feedbackRepository.findByInterviewRound_Id(roundId)
//                .orElseThrow(() -> new RuntimeException("Feedback not found for round: " + roundId));
//        return toResponse(fb);
//    }
//
//    public List<FeedbackResponse> getByRecruiter(Long recruiterId) {
//        return feedbackRepository.findBySubmittedBy_Id(recruiterId)
//                .stream().map(this::toResponse).collect(Collectors.toList());
//    }
//
//    public List<FeedbackResponse> getByCandidate(Long candidateId) {
//        return feedbackRepository.findByInterviewRound_Candidate_Id(candidateId)
//                .stream().map(this::toResponse).collect(Collectors.toList());
//    }
//
//    // ---- helpers ----
//    private Recruiter resolveRecruiter(Long explicitId) {
//        // Prefer authenticated principal
//        String principalEmail = getCurrentPrincipalEmail();
//        if (principalEmail != null) {
//            return recruiterRepository.findByEmail(principalEmail)
//                    .orElseGet(() -> explicitId != null ? recruiterRepository.findById(explicitId).orElse(null) : null);
//        }
//        if (explicitId != null) {
//            return recruiterRepository.findById(explicitId).orElse(null);
//        }
//        return null;
//    }
//
//    private String getCurrentPrincipalEmail() {
//        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth != null && auth.getPrincipal() instanceof String) {
//                return (String) auth.getPrincipal();
//            }
//        } catch (Exception ignored) {}
//        return null;
//    }
//
//    private void updateCandidateStageAfterFeedback(Candidate candidate, Job job, FeedbackResult result) {
//        if (candidate == null || job == null) return;
//
//        CandidateStage newStage;
//        switch (result) {
//            case SELECTED:
//                newStage = CandidateStage.SELECTED;
//                break;
//            case REJECTED:
//                newStage = CandidateStage.REJECTED;
//                break;
//            case HOLD:
//            default:
//                newStage = CandidateStage.SHORTLISTED;
//                break;
//        }
//
//        // upsert CandidateJob mapping
//        Optional<CandidateJob> cjOpt = candidateJobRepository.findByCandidateAndJob(candidate, job);
//        if (cjOpt.isPresent()) {
//            CandidateJob cj = cjOpt.get();
//            cj.setStage(newStage);
//            cj.setCurrentRound(null);
//            candidateJobRepository.save(cj);
//        } else {
//            CandidateJob cj = CandidateJob.builder()
//                    .candidate(candidate)
//                    .job(job)
//                    .stage(newStage)
//                    .appliedAt(java.time.LocalDateTime.now())
//                    .build();
//            candidateJobRepository.save(cj);
//        }
//    }
//
//    private FeedbackResponse toResponse(Feedback f) {
//        return FeedbackResponse.builder()
//                .id(f.getId())
//                .technicalRating(f.getTechnicalRating())
//                .communicationRating(f.getCommunicationRating())
//                .comments(f.getComments())
//                .result(f.getResult())
//                .interviewRoundId(f.getInterviewRound() != null ? f.getInterviewRound().getId() : null)
//                .submittedById(f.getSubmittedBy() != null ? f.getSubmittedBy().getId() : null)
//                .submittedAt(f.getSubmittedAt())
//                .build();
//    }
//}

package com.ims.fullstack.service;

import com.ims.fullstack.dto.feedback.FeedbackRequest;
import com.ims.fullstack.dto.feedback.FeedbackResponse;
import com.ims.fullstack.model.*;
import com.ims.fullstack.model.enums.CandidateStage;
import com.ims.fullstack.model.enums.FeedbackResult;
import com.ims.fullstack.repository.*;
import com.ims.fullstack.security.AuthUtil;      // ⭐ ADDED MISSING IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final InterviewRoundRepository roundRepository;
    private final RecruiterRepository recruiterRepository;
    private final CandidateJobRepository candidateJobRepository;
    private final MailService mailService;

    @Transactional
    public FeedbackResponse submitFeedback(Long roundId, FeedbackRequest req) {
        InterviewRound round = roundRepository.findById(roundId)
                .orElseThrow(() -> new RuntimeException("InterviewRound not found"));
        Recruiter submittedBy = resolveRecruiter(req.getSubmittedById());
        Feedback feedback = Feedback.builder()
                .technicalRating(req.getTechnicalRating())
                .communicationRating(req.getCommunicationRating())
                .comments(req.getComments())
                .result(req.getResult())
                .submittedAt(LocalDateTime.now())
                .submittedBy(submittedBy)
                .interviewRound(round)
                .build();
        Feedback saved = feedbackRepository.save(feedback);
        round.setFeedback(saved);
        round.setFeedbackSubmitted(true);
        roundRepository.save(round);
        updateCandidateStageAfterFeedback(round.getCandidate(), round.getJob(), req.getResult());
        try {
            Candidate c = round.getCandidate();
            if (c != null && c.getEmail() != null) {
                String subj = "Interview Update - " + req.getResult();
                String body;
                if (req.getResult() == FeedbackResult.SELECTED) {
                    body = "Hi " + c.getFullName() + ",\n\nGood news — you have been selected. HR will follow up.\n\nRegards,\nRecruitment Team";
                } else if (req.getResult() == FeedbackResult.REJECTED) {
                    body = "Hi " + c.getFullName() + ",\n\nThank you for your time. After evaluation, we will not be moving forward.\n\nRegards,\nRecruitment Team";
                } else {
                    body = "Hi " + c.getFullName() + ",\n\nYour application is on hold. We will update you soon.\n\nRegards,\nRecruitment Team";
                }
                mailService.sendInterviewInvite(c.getEmail(), subj, body, round);
            }
        } catch (Exception ignored) {}
        return toResponse(saved);
    }

    public FeedbackResponse getByRound(Long roundId) {
        Feedback fb = feedbackRepository.findByInterviewRound_Id(roundId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return toResponse(fb);
    }

    public List<FeedbackResponse> getByRecruiter(Long recruiterId) {
        return feedbackRepository.findBySubmittedBy_Id(recruiterId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<FeedbackResponse> getByCandidate(Long candidateId) {
        return feedbackRepository.findByInterviewRound_Candidate_Id(candidateId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private Recruiter resolveRecruiter(Long explicitId) {
        try {
            Long userId = AuthUtil.getUserId();
            if (userId != null) {
                return recruiterRepository.findById(userId).orElse(null);
            }
        } catch (Exception e) {
            // fall through
        }
        if (explicitId != null) {
            return recruiterRepository.findById(explicitId).orElse(null);
        }
        return null;
    }

    private void updateCandidateStageAfterFeedback(Candidate candidate, Job job, FeedbackResult result) {
        if (candidate == null || job == null) return;
        CandidateStage newStage;
        switch (result) {
            case SELECTED: newStage = CandidateStage.SELECTED; break;
            case REJECTED: newStage = CandidateStage.REJECTED; break;
            default: newStage = CandidateStage.SHORTLISTED; break;
        }
        candidateJobRepository.findByCandidateAndJob(candidate, job)
                .ifPresentOrElse(cj -> {
                    cj.setStage(newStage);
                    cj.setCurrentRound(null);
                    candidateJobRepository.save(cj);
                }, () -> {
                    CandidateJob cj = CandidateJob.builder()
                            .candidate(candidate)
                            .job(job)
                            .stage(newStage)
                            .appliedAt(LocalDateTime.now())
                            .build();
                    candidateJobRepository.save(cj);
                });
    }

    private FeedbackResponse toResponse(Feedback f) {
        return FeedbackResponse.builder()
                .id(f.getId())
                .technicalRating(f.getTechnicalRating())
                .communicationRating(f.getCommunicationRating())
                .comments(f.getComments())
                .result(f.getResult())
                .interviewRoundId(f.getInterviewRound() != null ? f.getInterviewRound().getId() : null)
                .submittedById(f.getSubmittedBy() != null ? f.getSubmittedBy().getId() : null)
                .submittedAt(f.getSubmittedAt())
                .build();
    }
}