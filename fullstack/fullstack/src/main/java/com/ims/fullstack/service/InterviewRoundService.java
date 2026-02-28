////package com.ims.fullstack.service;
////
////import com.ims.fullstack.dto.round.*;
////import com.ims.fullstack.model.*;
////import com.ims.fullstack.repository.*;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
////@RequiredArgsConstructor
////public class InterviewRoundService {
////
////    private final InterviewRoundRepository roundRepository;
////    private final JobRepository jobRepository;
////    private final CandidateRepository candidateRepository;
////    private final RecruiterRepository recruiterRepository;
////    private final MailService mailService; // optional notification
////
////    // Create round
////    @Transactional
////    public InterviewRoundResponse createRound(InterviewRoundCreateRequest req) {
////        Job job = jobRepository.findById(req.getJobId())
////                .orElseThrow(() -> new RuntimeException("Job not found: " + req.getJobId()));
////
////        Candidate candidate = candidateRepository.findById(req.getCandidateId())
////                .orElseThrow(() -> new RuntimeException("Candidate not found: " + req.getCandidateId()));
////
////        Recruiter recruiter = recruiterRepository.findById(req.getRecruiterId())
////                .orElseThrow(() -> new RuntimeException("Recruiter not found: " + req.getRecruiterId()));
////
////        InterviewRound round = InterviewRound.builder()
////                .roundName(req.getRoundName())
////                .roundType(req.getRoundType() != null ? req.getRoundType() : InterviewRound.builder().build().getRoundType())
////                .meetingLink(req.getMeetingLink())
////                .interviewTime(req.getInterviewTime())
////                .status(com.ims.fullstack.model.enums.InterviewStatus.SCHEDULED)
////                .feedbackSubmitted(false)
////                .candidate(candidate)
////                .job(job)
////                .recruiter(recruiter)
////                .build();
////
////        InterviewRound saved = roundRepository.save(round);
////
////        // Optional: notify candidate
////        try {
////            String subject = "Interview Round Scheduled: " + saved.getRoundName();
////            String body = String.format("Hi %s,\n\nYou have a scheduled interview round '%s' for '%s' on %s.\nMeeting Link: %s\n\nRegards,\nRecruitment Team",
////                    candidate.getFullName() != null ? candidate.getFullName() : candidate.getEmail(),
////                    saved.getRoundName(),
////                    job.getTitle(),
////                    saved.getInterviewTime(),
////                    saved.getMeetingLink()
////            );
////            mailService.sendInterviewInvite(candidate.getEmail(), subject, body, saved);
////        } catch (Exception ex) {
////            // swallow - logging would be ideal
////        }
////
////        return toResponse(saved);
////    }
////
////    // Update round
////    @Transactional
////    public InterviewRoundResponse updateRound(Long id, InterviewRoundUpdateRequest req) {
////        InterviewRound existing = roundRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("InterviewRound not found: " + id));
////
////        if (req.getRoundName() != null) existing.setRoundName(req.getRoundName());
////        if (req.getRoundType() != null) existing.setRoundType(req.getRoundType());
////        if (req.getInterviewTime() != null) existing.setInterviewTime(req.getInterviewTime());
////        if (req.getMeetingLink() != null) existing.setMeetingLink(req.getMeetingLink());
////        if (req.getFeedbackSubmitted() != null) existing.setFeedbackSubmitted(req.getFeedbackSubmitted());
////
////        InterviewRound saved = roundRepository.save(existing);
////        return toResponse(saved);
////    }
////
////    // Change status (SCHEDULED / COMPLETED / CANCELLED)
////    @Transactional
////    public InterviewRoundResponse changeStatus(Long id, String statusStr) {
////        InterviewRound existing = roundRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("InterviewRound not found: " + id));
////
////        com.ims.fullstack.model.enums.InterviewStatus status =
////                com.ims.fullstack.model.enums.InterviewStatus.valueOf(statusStr.toUpperCase());
////
////        existing.setStatus(status);
////        InterviewRound saved = roundRepository.save(existing);
////
////        // Optionally notify candidate on completion/cancellation
////        try {
////            Candidate c = saved.getCandidate();
////            if (c != null && c.getEmail() != null) {
////                String subj = "Interview Round " + status + " - " + saved.getRoundName();
////                String body = String.format("Hi %s,\n\nYour interview round '%s' is now %s.\n\nRegards,\nTeam",
////                        c.getFullName() != null ? c.getFullName() : c.getEmail(),
////                        saved.getRoundName(), status);
////                mailService.sendInterviewInvite(c.getEmail(), subj, body, saved);
////            }
////        } catch (Exception e) {
////            // ignore
////        }
////
////        return toResponse(saved);
////    }
////
////    // Delete round
////    public void deleteRound(Long id) {
////        InterviewRound existing = roundRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("InterviewRound not found: " + id));
////        roundRepository.delete(existing);
////    }
////
////    // Get rounds by job
////    public List<InterviewRoundResponse> getByJob(Long jobId) {
////        return roundRepository.findByJob_Id(jobId)
////                .stream().map(this::toResponse).collect(Collectors.toList());
////    }
////
////    // Get rounds by candidate
////    public List<InterviewRoundResponse> getByCandidate(Long candidateId) {
////        return roundRepository.findByCandidate_CandidateId(candidateId)
////                .stream().map(this::toResponse).collect(Collectors.toList());
////    }
////
////    // Get rounds by recruiter
////    public List<InterviewRoundResponse> getByRecruiter(Long recruiterId) {
////        return roundRepository.findByRecruiter_Id(recruiterId)
////                .stream().map(this::toResponse).collect(Collectors.toList());
////    }
////
////    // Helper: convert entity to DTO
////    private InterviewRoundResponse toResponse(InterviewRound r) {
////        return InterviewRoundResponse.builder()
////                .id(r.getId())
////                .roundName(r.getRoundName())
////                .roundType(r.getRoundType())
////                .interviewTime(r.getInterviewTime())
////                .meetingLink(r.getMeetingLink())
////                .status(r.getStatus())
////                .feedbackSubmitted(r.getFeedbackSubmitted())
////                .candidateId(r.getCandidate() != null ? r.getCandidate().getCandidateId() : null)
////                .jobId(r.getJob() != null ? r.getJob().getId() : null)
////                .recruiterId(r.getRecruiter() != null ? r.getRecruiter().getId() : null)
////                .build();
////    }
////}
//
////package com.ims.fullstack.service;
////
////import com.ims.fullstack.dto.round.InterviewRoundCreateRequest;
////import com.ims.fullstack.dto.round.InterviewRoundResponse;
////import com.ims.fullstack.dto.round.InterviewRoundUpdateRequest;
////import com.ims.fullstack.model.*;
////import com.ims.fullstack.model.enums.InterviewStatus;
////import com.ims.fullstack.repository.*;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
////@RequiredArgsConstructor
////public class InterviewRoundService {
////
////    private final InterviewRoundRepository roundRepository;
////    private final JobRepository jobRepository;
////    private final CandidateRepository candidateRepository;
////    private final RecruiterRepository recruiterRepository;
////    private final MailService mailService;
////
////    // ---------------------------------------------------
////    // CREATE INTERVIEW ROUND
////    // ---------------------------------------------------
////    @Transactional
////    public InterviewRoundResponse createRound(InterviewRoundCreateRequest req) {
////
////        Job job = jobRepository.findById(req.getJobId())
////                .orElseThrow(() -> new RuntimeException("Job not found"));
////
////        Candidate candidate = candidateRepository.findById(req.getCandidateId())
////                .orElseThrow(() -> new RuntimeException("Candidate not found"));
////
////        Recruiter recruiter = recruiterRepository.findById(req.getRecruiterId())
////                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
////
////        InterviewRound round = InterviewRound.builder()
////                .roundName(req.getRoundName())
////                .roundType(req.getRoundType())
////                .meetingLink(req.getMeetingLink())
////                .interviewTime(req.getInterviewTime())
////                .status(InterviewStatus.SCHEDULED)
////                .feedbackSubmitted(false)
////                .candidate(candidate)
////                .job(job)
////                .recruiter(recruiter)
////                .build();
////
////        InterviewRound saved = roundRepository.save(round);
////
////        // Optional mail
////        try {
////            mailService.sendInterviewInvite(
////                    candidate.getEmail(),
////                    "Interview Round Scheduled",
////                    "Hi " + candidate.getFullName() +
////                            ",\n\nYour interview round '" + saved.getRoundName() +
////                            "' is scheduled.\n\nRegards,\nRecruitment Team",
////                    saved
////            );
////        } catch (Exception ignored) {}
////
////        return toResponse(saved);
////    }
////
////    // ---------------------------------------------------
////    // UPDATE ROUND
////    // ---------------------------------------------------
////    @Transactional
////    public InterviewRoundResponse updateRound(Long id, InterviewRoundUpdateRequest req) {
////
////        InterviewRound existing = roundRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Interview round not found"));
////
////        if (req.getRoundName() != null) existing.setRoundName(req.getRoundName());
////        if (req.getRoundType() != null) existing.setRoundType(req.getRoundType());
////        if (req.getInterviewTime() != null) existing.setInterviewTime(req.getInterviewTime());
////        if (req.getMeetingLink() != null) existing.setMeetingLink(req.getMeetingLink());
////        if (req.getFeedbackSubmitted() != null)
////            existing.setFeedbackSubmitted(req.getFeedbackSubmitted());
////
////        return toResponse(roundRepository.save(existing));
////    }
////
////    // ---------------------------------------------------
////    // CHANGE STATUS
////    // ---------------------------------------------------
////    @Transactional
////    public InterviewRoundResponse changeStatus(Long id, String status) {
////
////        InterviewRound round = roundRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Interview round not found"));
////
////        round.setStatus(InterviewStatus.valueOf(status.toUpperCase()));
////        return toResponse(roundRepository.save(round));
////    }
////
////    // ---------------------------------------------------
////    // DELETE
////    // ---------------------------------------------------
////    public void deleteRound(Long id) {
////        roundRepository.deleteById(id);
////    }
////
////    // ---------------------------------------------------
////    // GETTERS
////    // ---------------------------------------------------
////    public List<InterviewRoundResponse> getByJob(Long jobId) {
////        return roundRepository.findByJob_Id(jobId)
////                .stream()
////                .map(this::toResponse)
////                .collect(Collectors.toList());
////    }
////
////    public List<InterviewRoundResponse> getByCandidate(Long candidateId) {
////        return roundRepository.findByCandidate_Id(candidateId)
////                .stream()
////                .map(this::toResponse)
////                .collect(Collectors.toList());
////    }
////
////    public List<InterviewRoundResponse> getByRecruiter(Long recruiterId) {
////        return roundRepository.findByRecruiter_Id(recruiterId)
////                .stream()
////                .map(this::toResponse)
////                .collect(Collectors.toList());
////    }
////
////    // ---------------------------------------------------
////    // ENTITY → DTO
////    // ---------------------------------------------------
////    private InterviewRoundResponse toResponse(InterviewRound r) {
////        return InterviewRoundResponse.builder()
////                .id(r.getId())
////                .roundName(r.getRoundName())
////                .roundType(r.getRoundType())
////                .interviewTime(r.getInterviewTime())
////                .meetingLink(r.getMeetingLink())
////                .status(r.getStatus())
////                .feedbackSubmitted(r.getFeedbackSubmitted())
////                .candidateId(
////                        r.getCandidate() != null ? r.getCandidate().getId() : null
////                )
////                .jobId(
////                        r.getJob() != null ? r.getJob().getId() : null
////                )
////                .recruiterId(
////                        r.getRecruiter() != null ? r.getRecruiter().getId() : null
////                )
////                .build();
////    }
////}
//
//
//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.round.*;
//import com.ims.fullstack.model.*;
//import com.ims.fullstack.model.enums.InterviewStatus;
//import com.ims.fullstack.repository.*;
//import com.ims.fullstack.security.AuthUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class InterviewRoundService {
//
//    private final InterviewRoundRepository roundRepository;
//    private final JobRepository jobRepository;
//    private final CandidateRepository candidateRepository;
//    private final RecruiterRepository recruiterRepository;
//    private final MailService mailService;
//
//    // =============================
//    // CREATE ROUND (Recruiter)
//    // =============================
//    @Transactional
//    public InterviewRoundResponse createRound(InterviewRoundCreateRequest req) {
//
//        Job job = jobRepository.findById(req.getJobId())
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        Candidate candidate = candidateRepository.findById(req.getCandidateId())
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//
//        // 🔐 recruiter enforced from JWT
//        Recruiter recruiter = recruiterRepository.findById(AuthUtil.getUserId())
//                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
//
//        InterviewRound round = InterviewRound.builder()
//                .roundName(req.getRoundName())
//                .roundType(req.getRoundType())
//                .meetingLink(req.getMeetingLink())
//                .interviewTime(req.getInterviewTime())
//                .status(InterviewStatus.SCHEDULED)
//                .feedbackSubmitted(false)
//                .candidate(candidate)
//                .job(job)
//                .recruiter(recruiter)
//                .build();
//
//        InterviewRound saved = roundRepository.save(round);
//
//        try {
//            mailService.sendInterviewInvite(
//                    candidate.getEmail(),
//                    "Interview Round Scheduled",
//                    "Hi " + candidate.getFullName() +
//                            ",\n\nYour interview round '" + saved.getRoundName() +
//                            "' is scheduled.\n\nRegards,\nRecruitment Team",
//                    saved
//            );
//        } catch (Exception ignored) {}
//
//        return toResponse(saved);
//    }
//
//    // =============================
//    // UPDATE ROUND
//    // =============================
//    @Transactional
//    public InterviewRoundResponse updateRound(Long id, InterviewRoundUpdateRequest req) {
//
//        InterviewRound existing = roundRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Interview round not found"));
//
//        if (req.getRoundName() != null) existing.setRoundName(req.getRoundName());
//        if (req.getRoundType() != null) existing.setRoundType(req.getRoundType());
//        if (req.getInterviewTime() != null) existing.setInterviewTime(req.getInterviewTime());
//        if (req.getMeetingLink() != null) existing.setMeetingLink(req.getMeetingLink());
//        if (req.getFeedbackSubmitted() != null)
//            existing.setFeedbackSubmitted(req.getFeedbackSubmitted());
//
//        return toResponse(roundRepository.save(existing));
//    }
//
//    // =============================
//    // CHANGE STATUS
//    // =============================
//    @Transactional
//    public InterviewRoundResponse changeStatus(Long id, String status) {
//
//        InterviewRound round = roundRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Interview round not found"));
//
//        round.setStatus(InterviewStatus.valueOf(status.toUpperCase()));
//        return toResponse(roundRepository.save(round));
//    }
//
//    // =============================
//    // DELETE
//    // =============================
//    public void deleteRound(Long id) {
//        roundRepository.deleteById(id);
//    }
//
//    // =============================
//    // 🔐 Recruiter views
//    // =============================
//    public List<InterviewRoundResponse> getMyRounds() {
//        return roundRepository.findByRecruiter_Id(AuthUtil.getUserId())
//                .stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    // =============================
//    // Read-only
//    // =============================
//    public List<InterviewRoundResponse> getByJob(Long jobId) {
//        return roundRepository.findByJob_Id(jobId)
//                .stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    public List<InterviewRoundResponse> getByCandidate(Long candidateId) {
//        return roundRepository.findByCandidate_Id(candidateId)
//                .stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    // =============================
//    // ENTITY → DTO
//    // =============================
//    private InterviewRoundResponse toResponse(InterviewRound r) {
//        return InterviewRoundResponse.builder()
//                .id(r.getId())
//                .roundName(r.getRoundName())
//                .roundType(r.getRoundType())
//                .interviewTime(r.getInterviewTime())
//                .meetingLink(r.getMeetingLink())
//                .status(r.getStatus())
//                .feedbackSubmitted(r.getFeedbackSubmitted())
//                .candidateId(
//                        r.getCandidate() != null ? r.getCandidate().getId() : null
//                )
//                .jobId(
//                        r.getJob() != null ? r.getJob().getId() : null
//                )
//                .recruiterId(
//                        r.getRecruiter() != null ? r.getRecruiter().getId() : null
//                )
//                .build();
//    }
//}
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