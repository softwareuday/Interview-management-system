
package com.ims.fullstack.service;

import com.ims.fullstack.dto.round.*;
import com.ims.fullstack.model.*;
import com.ims.fullstack.model.enums.InterviewStatus;
import com.ims.fullstack.repository.*;
import com.ims.fullstack.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewRoundService {
    private final InterviewRoundRepository roundRepository;
    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final MailService mailService;

    @Transactional
    public InterviewRoundResponse createRound(InterviewRoundCreateRequest req) {
        Job job = jobRepository.findById(req.getJobId()).orElseThrow(() -> new RuntimeException("Job not found"));
        Candidate candidate = candidateRepository.findById(req.getCandidateId()).orElseThrow(() -> new RuntimeException("Candidate not found"));
        Recruiter recruiter = recruiterRepository.findById(AuthUtil.getUserId()).orElseThrow(() -> new RuntimeException("Recruiter not found"));
        InterviewRound round = InterviewRound.builder()
                .roundName(req.getRoundName())
                .roundType(req.getRoundType())
                .meetingLink(req.getMeetingLink())
                .interviewTime(req.getInterviewTime())
                .status(InterviewStatus.SCHEDULED)
                .feedbackSubmitted(false)
                .candidate(candidate)
                .job(job)
                .recruiter(recruiter)
                .build();
        InterviewRound saved = roundRepository.save(round);
        try {
            mailService.sendInterviewInvite(candidate.getEmail(),
                    "Interview Round Scheduled",
                    "Hi " + candidate.getFullName() + ",\n\nYour interview round '" + saved.getRoundName() + "' is scheduled.\n\nRegards,\nRecruitment Team");
        } catch (Exception ignored) {}
        return toResponse(saved);
    }

    public List<InterviewRoundResponse> getMyRounds() {
        return roundRepository.findByRecruiter_Id(AuthUtil.getUserId()).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<InterviewRoundResponse> getByCandidate(Long candidateId) {
        return roundRepository.findByCandidate_Id(candidateId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public InterviewRoundResponse updateRound(Long id, InterviewRoundUpdateRequest req) {
        InterviewRound existing = roundRepository.findById(id).orElseThrow(() -> new RuntimeException("Round not found"));
        if (req.getRoundName() != null) existing.setRoundName(req.getRoundName());
        if (req.getRoundType() != null) existing.setRoundType(req.getRoundType());
        if (req.getInterviewTime() != null) existing.setInterviewTime(req.getInterviewTime());
        if (req.getMeetingLink() != null) existing.setMeetingLink(req.getMeetingLink());
        if (req.getFeedbackSubmitted() != null) existing.setFeedbackSubmitted(req.getFeedbackSubmitted());
        return toResponse(roundRepository.save(existing));
    }

    public InterviewRoundResponse changeStatus(Long id, String status) {
        InterviewRound round = roundRepository.findById(id).orElseThrow(() -> new RuntimeException("Round not found"));
        round.setStatus(InterviewStatus.valueOf(status.toUpperCase()));
        return toResponse(roundRepository.save(round));
    }

    public void deleteRound(Long id) {
        roundRepository.deleteById(id);
    }

    private InterviewRoundResponse toResponse(InterviewRound r) {
        return InterviewRoundResponse.builder()
                .id(r.getId())
                .roundName(r.getRoundName())
                .roundType(r.getRoundType())
                .interviewTime(r.getInterviewTime())
                .meetingLink(r.getMeetingLink())
                .status(r.getStatus())
                .feedbackSubmitted(r.getFeedbackSubmitted())
                .candidateId(r.getCandidate() != null ? r.getCandidate().getId() : null)
                .jobId(r.getJob() != null ? r.getJob().getId() : null)
                .recruiterId(r.getRecruiter() != null ? r.getRecruiter().getId() : null)
                .build();
    }
}