//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.round.InterviewRoundCreateRequest;
//import com.ims.fullstack.dto.round.InterviewRoundResponse;
//import com.ims.fullstack.dto.round.InterviewRoundUpdateRequest;
//import com.ims.fullstack.model.*;
//import com.ims.fullstack.model.enums.CandidateStage;
//import com.ims.fullstack.model.enums.InterviewStatus;
//import com.ims.fullstack.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class RoundWorkflowService {
//
//    private final InterviewRoundRepository roundRepository;
//    private final CandidateJobRepository candidateJobRepository;
//    private final JobRepository jobRepository;
//    private final CandidateRepository candidateRepository;
//    private final RecruiterRepository recruiterRepository;
//    private final MailService mailService;
//
//    /**
//     * Complete a round. Optionally create next round from template.
//     * Returns the created next round DTO if created, otherwise null.
//     */
//    @Transactional
//    public InterviewRoundResponse completeRound(Long roundId, InterviewRoundCreateRequest nextRoundTemplate) {
//        InterviewRound existing = roundRepository.findById(roundId)
//                .orElseThrow(() -> new RuntimeException("InterviewRound not found: " + roundId));
//
//        // avoid double-completing
//        if (existing.getStatus() == InterviewStatus.COMPLETED) {
//            throw new RuntimeException("InterviewRound already completed: " + roundId);
//        }
//
//        // mark completed
//        existing.setStatus(InterviewStatus.COMPLETED);
//        existing.setFeedbackSubmitted(existing.getFeedbackSubmitted() != null ? existing.getFeedbackSubmitted() : false);
//        InterviewRound saved = roundRepository.save(existing);
//
//        // Notify candidate about completion
//        try {
//            Candidate cand = saved.getCandidate();
//            if (cand != null && cand.getEmail() != null) {
//                String subject = "Your interview round completed: " + saved.getRoundName();
//                String body = String.format("Hi %s,\n\nYour interview round '%s' for '%s' was marked as COMPLETED.\n\nRegards,\nRecruitment Team",
//                        cand.getFullName() != null ? cand.getFullName() : cand.getEmail(),
//                        saved.getRoundName(),
//                        saved.getJob() != null ? saved.getJob().getTitle() : "the job");
//                mailService.sendInterviewInvite(cand.getEmail(), subject, body, saved);
//            }
//        } catch (Exception ex) {
//            // swallow but log ideally
//        }
//
//        // If a template is supplied, create the next round (automatic)
//        InterviewRoundResponse createdNext = null;
//        if (nextRoundTemplate != null) {
//            createdNext = createNextRoundFromTemplate(saved, nextRoundTemplate);
//            // update candidate-job stage to INTERVIEW_SCHEDULED
//            updateCandidateJobStage(saved.getCandidate(), saved.getJob(), CandidateStage.INTERVIEW_SCHEDULED);
//        } else {
//            // No next round created automatically. We'll set candidate stage to SHORTLISTED (business decision)
//            updateCandidateJobStage(saved.getCandidate(), saved.getJob(), CandidateStage.SHORTLISTED);
//        }
//
//        return createdNext;
//    }
//
//    /**
//     * Create next round using a template object. Returns response DTO for created round.
//     */
//    @Transactional
//    public InterviewRoundResponse createNextRoundFromTemplate(InterviewRound fromRound, InterviewRoundCreateRequest template) {
//
//        // fetch candidate/job/recruiter entities (they should already exist)
//        Candidate candidate = fromRound.getCandidate();
//        Job job = fromRound.getJob();
//        Recruiter recruiter = fromRound.getRecruiter();
//
//        if (candidate == null || job == null || recruiter == null) {
//            throw new RuntimeException("Cannot create next round because candidate/job/recruiter is missing on the previous round");
//        }
//
//        InterviewRound newRound = InterviewRound.builder()
//                .roundName(template.getRoundName() != null ? template.getRoundName() : "Next Round")
//                .roundType(template.getRoundType() != null ? template.getRoundType() : fromRound.getRoundType())
//                .meetingLink(template.getMeetingLink())
//                .interviewTime(template.getInterviewTime())
//                .status(com.ims.fullstack.model.enums.InterviewStatus.SCHEDULED)
//                .feedbackSubmitted(false)
//                .candidate(candidate)
//                .job(job)
//                .recruiter(recruiter)
//                .build();
//
//        InterviewRound saved = roundRepository.save(newRound);
//
//        // notify candidate about the next round
//        try {
//            mailService.sendInterviewInvite(candidate.getEmail(),
//                    "Next Interview Round Scheduled: " + saved.getRoundName(),
//                    "Hi " + (candidate.getFullName() != null ? candidate.getFullName() : candidate.getEmail())
//                            + ",\n\nYour next interview round '" + saved.getRoundName()
//                            + "' is scheduled on " + saved.getInterviewTime()
//                            + ".\nMeeting link: " + saved.getMeetingLink()
//                    , saved);
//        } catch (Exception ex) {
//            // swallow
//        }
//
//        return toResponse(saved);
//    }
//
//    /**
//     * Manual creation of next round from a template (called by recruiter).
//     */
//    @Transactional
//    public InterviewRoundResponse manualCreateNext(Long currentRoundId, InterviewRoundCreateRequest template) {
//        InterviewRound fromRound = roundRepository.findById(currentRoundId)
//                .orElseThrow(() -> new RuntimeException("InterviewRound not found: " + currentRoundId));
//        InterviewRoundResponse created = createNextRoundFromTemplate(fromRound, template);
//        // update stage to INTERVIEW_SCHEDULED
//        updateCandidateJobStage(fromRound.getCandidate(), fromRound.getJob(), CandidateStage.INTERVIEW_SCHEDULED);
//        return created;
//    }
//
//    private void updateCandidateJobStage(Candidate candidate, Job job, CandidateStage stage) {
//        if (candidate == null || job == null) return;
//
//        Optional<CandidateJob> cjOpt = candidateJobRepository.findByCandidateAndJob(candidate, job);
//        if (cjOpt.isPresent()) {
//            CandidateJob cj = cjOpt.get();
//            cj.setStage(stage);
//            candidateJobRepository.save(cj);
//        } else {
//            // If no CandidateJob exists yet, create one (useful if candidate applied via external flow)
//            CandidateJob cj = CandidateJob.builder()
//                    .candidate(candidate)
//                    .job(job)
//                    .stage(stage)
//                    .currentRound(null)
//                    .build();
//            candidateJobRepository.save(cj);
//        }
//    }
//
//    private InterviewRoundResponse toResponse(InterviewRound r) {
//        return InterviewRoundResponse.builder()
//                .id(r.getId())
//                .roundName(r.getRoundName())
//                .roundType(r.getRoundType())
//                .interviewTime(r.getInterviewTime())
//                .meetingLink(r.getMeetingLink())
//                .status(r.getStatus())
//                .feedbackSubmitted(r.getFeedbackSubmitted())
//                .candidateId(r.getCandidate() != null ? r.getCandidate().getCandidateId() : null)
//                .jobId(r.getJob() != null ? r.getJob().getId() : null)
//                .recruiterId(r.getRecruiter() != null ? r.getRecruiter().getId() : null)
//                .build();
//    }
//}
