////package com.ims.fullstack.service;
////
////import com.ims.fullstack.model.Candidate;
////import com.ims.fullstack.model.Interview;
////import com.ims.fullstack.repository.InterviewRepository;
////import com.ims.fullstack.repository.CandidateRepository;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.time.LocalDate;
////import java.time.LocalDateTime;
////import java.util.List;
////import java.util.UUID;
////
////@Service
////@RequiredArgsConstructor
////public class InterviewService {
////
////    private final InterviewRepository interviewRepo;
////    private final CandidateRepository candidateRepo;
////    private final FileStorageService fileStorageService;
////    private final MailService mailService;
////    private final CandidateService candidateService;
////
////    /**
////     * Schedule an interview. This method:
////     * - ensures candidate exists (creates external candidate if necessary)
////     * - stores resume if provided
////     * - saves interview with status SCHEDULED
////     * - sends an email invite to the candidate
////     */
////    public Interview scheduleInterview(Interview interview, MultipartFile resumeFile) {
////        String recruiterIdentifier = interview.getCreatedBy() != null ? interview.getCreatedBy()
////                : (interview.getRecruiterId() != null ? String.valueOf(interview.getRecruiterId()) : "RECRUITER");
////
////        Candidate candidate = candidateRepo.findByEmail(interview.getCandidate().getEmail())
////                .orElseGet(() -> candidateService.findOrCreateExternalCandidate(interview.getCandidate(), recruiterIdentifier));
////
////        // Store resume if uploaded
////        if (resumeFile != null && !resumeFile.isEmpty()) {
////            String resumeUrl = fileStorageService.storeFile(resumeFile);
////            candidate.setResumeUrl(resumeUrl);
////            candidate.setLastUpdatedBy(recruiterIdentifier);
////            candidateRepo.save(candidate);
////            interview.setResumeUrl(resumeUrl);
////        }
////
////        // Ensure candidate fields are linked
////        interview.setCandidate(candidate);
////        interview.setStatus(interview.getStatus() == null ? "SCHEDULED" : interview.getStatus());
////
////        // Persist interview
////        Interview saved = interviewRepo.save(interview);
////
////        // Send email notification (best-effort; MailService handles exceptions)
////        try {
////            String subject = "Interview Scheduled - " + (interview.getPosition() != null ? interview.getPosition() : "Interview");
////            String body = String.format(
////                    "Hi %s,\n\nYour interview has been scheduled.\n\nDate: %s\nTime: %s\nMode: %s\nMeeting Link: %s\n\nBest regards,\nRecruitment Team",
////                    candidate.getFullName() != null ? candidate.getFullName() : candidate.getEmail(),
////                    interview.getInterviewDate(),
////                    interview.getInterviewTime(),
////                    interview.getMode(),
////                    interview.getMeetingLink()
////            );
////            mailService.sendInterviewInvite(candidate.getEmail(), subject, body);
////        } catch (Exception ex) {
////            // don't fail scheduling because email failed; optionally log it
////            // You can inject EmailLogRepository and save a failure log here if desired
////        }
////
////        return saved;
////    }
////
////    /**
////     * Update interview details. Only basic fields updated here (position, date, time, mode, meeting link, remarks).
////     * You can expand validation/authorization as needed.
////     */
////    public Interview updateInterview(Long id, Interview updated) {
////        Interview existing = interviewRepo.findById(id).orElseThrow(() -> new RuntimeException("Interview not found: " + id));
////
////        if (updated.getPosition() != null) existing.setPosition(updated.getPosition());
////        if (updated.getInterviewDate() != null) existing.setInterviewDate(updated.getInterviewDate());
////        if (updated.getInterviewTime() != null) existing.setInterviewTime(updated.getInterviewTime());
////        if (updated.getMode() != null) existing.setMode(updated.getMode());
////        if (updated.getMeetingLink() != null) existing.setMeetingLink(updated.getMeetingLink());
////        if (updated.getRemarks() != null) existing.setRemarks(updated.getRemarks());
////        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
////
////        // If candidate info in update payload (rare), update candidate lastUpdatedBy
////        if (updated.getCandidate() != null && updated.getCandidate().getEmail() != null) {
////            Candidate c = candidateRepo.findByEmail(updated.getCandidate().getEmail()).orElse(null);
////            if (c != null) {
////                c.setLastUpdatedBy(updated.getCreatedBy() != null ? updated.getCreatedBy() : c.getLastUpdatedBy());
////                candidateRepo.save(c);
////                existing.setCandidate(c);
////            }
////        }
////
////        Interview saved = interviewRepo.save(existing);
////
////        // Optional: send update notification to candidate
////        return saved;
////    }
////
////    public List<Interview> getAllByRecruiter(Long recruiterId) {
////        return interviewRepo.findByRecruiterId(recruiterId);
////    }
////
////    public List<Interview> getAllByCandidate(Long candidateId) {
////        return interviewRepo.findByCandidate_CandidateId(candidateId);
////    }
////
////    public List<Interview> getUpcomingByRecruiter(Long recruiterId) {
////        LocalDate today = LocalDate.now();
////        return interviewRepo.findByRecruiterIdAndInterviewDateAfter(recruiterId, today.minusDays(1));
////    }
////
////    public List<Interview> getPastByRecruiter(Long recruiterId) {
////        LocalDate today = LocalDate.now();
////        return interviewRepo.findByRecruiterIdAndInterviewDateBefore(recruiterId, today);
////    }
////
////    public Interview updateStatus(Long id, String status) {
////        Interview interview = interviewRepo.findById(id).orElseThrow(() -> new RuntimeException("Interview not found: " + id));
////        interview.setStatus(status);
////        return interviewRepo.save(interview);
////    }
////
////    public void cancelInterview(Long id) {
////        Interview interview = interviewRepo.findById(id).orElseThrow(() -> new RuntimeException("Interview not found: " + id));
////        interview.setStatus("CANCELLED");
////        interviewRepo.save(interview);
////
////        // Optionally notify candidate about cancellation
////        try {
////            Candidate c = interview.getCandidate();
////            if (c != null && c.getEmail() != null) {
////                String subject = "Interview Cancelled - " + (interview.getPosition() != null ? interview.getPosition() : "Interview");
////                String body = String.format(
////                        "Hi %s,\n\nYour interview scheduled on %s at %s has been cancelled.\n\nRegards,\nRecruitment Team",
////                        c.getFullName() != null ? c.getFullName() : c.getEmail(),
////                        interview.getInterviewDate(), interview.getInterviewTime()
////                );
////                mailService.sendInterviewInvite(c.getEmail(), subject, body);
////            }
////        } catch (Exception ex) {
////            // swallow email exceptions
////        }
////    }
////}
////
////package com.ims.fullstack.service;
////
////import com.ims.fullstack.model.Candidate;
////import com.ims.fullstack.model.Interview;
////import com.ims.fullstack.repository.InterviewRepository;
////import com.ims.fullstack.repository.CandidateRepository;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.time.LocalDate;
////import java.util.List;
////
////@Service
////@RequiredArgsConstructor
////public class InterviewService {
////
////    private final InterviewRepository interviewRepo;
////    private final CandidateRepository candidateRepo;
////    private final FileStorageService fileStorageService;
////    private final MailService mailService;
////    private final CandidateService candidateService;
////
////    /**
////     * Schedule interview for a candidate.
////     * Candidate MUST already exist (registered user).
////     */
////    public Interview scheduleInterview(Interview interview, MultipartFile resumeFile) {
////
////        // 🔐 Candidate must already exist (Phase 1 rule)
////        Candidate candidate = candidateRepo.findByEmail(interview.getCandidate().getEmail())
////                .orElseThrow(() -> new RuntimeException("Candidate not found"));
////
////        // Optional resume upload
////        if (resumeFile != null && !resumeFile.isEmpty()) {
////            String resumeUrl = fileStorageService.storeFile(resumeFile);
////            candidate.setResumeUrl(resumeUrl);
////            candidateRepo.save(candidate);
////            interview.setResumeUrl(resumeUrl);
////        }
////
////        interview.setCandidate(candidate);
////        interview.setStatus(interview.getStatus() == null ? "SCHEDULED" : interview.getStatus());
////
////        Interview saved = interviewRepo.save(interview);
////
////        // Notify candidate (best effort)
////        try {
////            mailService.sendInterviewInvite(
////                    candidate.getEmail(),
////                    "Interview Scheduled",
////                    "Hi,\n\nYour interview has been scheduled.\n\nRegards,\nRecruitment Team"
////            );
////        } catch (Exception ignored) {}
////
////        return saved;
////    }
////
////    public Interview updateInterview(Long id, Interview updated) {
////        Interview existing = interviewRepo.findById(id)
////                .orElseThrow(() -> new RuntimeException("Interview not found"));
////
////        if (updated.getPosition() != null) existing.setPosition(updated.getPosition());
////        if (updated.getInterviewDate() != null) existing.setInterviewDate(updated.getInterviewDate());
////        if (updated.getInterviewTime() != null) existing.setInterviewTime(updated.getInterviewTime());
////        if (updated.getMode() != null) existing.setMode(updated.getMode());
////        if (updated.getMeetingLink() != null) existing.setMeetingLink(updated.getMeetingLink());
////        if (updated.getRemarks() != null) existing.setRemarks(updated.getRemarks());
////        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
////
////        return interviewRepo.save(existing);
////    }
////
////    public List<Interview> getAllByRecruiter(Long recruiterId) {
////        return interviewRepo.findByRecruiterId(recruiterId);
////    }
////
////    public List<Interview> getAllByCandidate(Long candidateId) {
////        return interviewRepo.findByCandidate_Id(candidateId);
////    }
////
////    public List<Interview> getUpcomingByRecruiter(Long recruiterId) {
////        LocalDate today = LocalDate.now();
////        return interviewRepo.findByRecruiterIdAndInterviewDateAfter(recruiterId, today.minusDays(1));
////    }
////
////    public List<Interview> getPastByRecruiter(Long recruiterId) {
////        LocalDate today = LocalDate.now();
////        return interviewRepo.findByRecruiterIdAndInterviewDateBefore(recruiterId, today);
////    }
////
////    public Interview updateStatus(Long id, String status) {
////        Interview interview = interviewRepo.findById(id)
////                .orElseThrow(() -> new RuntimeException("Interview not found"));
////        interview.setStatus(status);
////        return interviewRepo.save(interview);
////    }
////
////    public void cancelInterview(Long id) {
////        Interview interview = interviewRepo.findById(id)
////                .orElseThrow(() -> new RuntimeException("Interview not found"));
////        interview.setStatus("CANCELLED");
////        interviewRepo.save(interview);
////    }
////}
//
//package com.ims.fullstack.service;
//
//import com.ims.fullstack.model.Candidate;
//import com.ims.fullstack.model.Interview;
//import com.ims.fullstack.repository.InterviewRepository;
//import com.ims.fullstack.repository.CandidateRepository;
//import com.ims.fullstack.security.AuthUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class InterviewService {
//
//    private final InterviewRepository interviewRepo;
//    private final CandidateRepository candidateRepo;
//    private final FileStorageService fileStorageService;
//    private final MailService mailService;
//
//    // =============================
//    // CREATE INTERVIEW (Recruiter)
//    // =============================
//    public Interview scheduleInterview(Interview interview, MultipartFile resumeFile) {
//
//        // 🔐 enforce recruiter identity from JWT
//        interview.setRecruiterId(AuthUtil.getUserId());
//
//        Candidate candidate = candidateRepo.findByEmail(
//                interview.getCandidate().getEmail()
//        ).orElseThrow(() -> new RuntimeException("Candidate not found"));
//
//        if (resumeFile != null && !resumeFile.isEmpty()) {
//            String resumeUrl = fileStorageService.storeFile(resumeFile);
//            candidate.setResumeUrl(resumeUrl);
//            candidateRepo.save(candidate);
//            interview.setResumeUrl(resumeUrl);
//        }
//
//        interview.setCandidate(candidate);
//        interview.setStatus(
//                interview.getStatus() == null ? "SCHEDULED" : interview.getStatus()
//        );
//
//        Interview saved = interviewRepo.save(interview);
//
//        try {
//            mailService.sendInterviewInvite(
//                    candidate.getEmail(),
//                    "Interview Scheduled",
//                    "Hi,\n\nYour interview has been scheduled.\n\nRegards,\nRecruitment Team"
//            );
//        } catch (Exception ignored) {}
//
//        return saved;
//    }
//
//    // =============================
//    // UPDATE INTERVIEW
//    // =============================
//    public Interview updateInterview(Long id, Interview updated) {
//        Interview existing = interviewRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Interview not found"));
//
//        if (updated.getPosition() != null) existing.setPosition(updated.getPosition());
//        if (updated.getInterviewDate() != null) existing.setInterviewDate(updated.getInterviewDate());
//        if (updated.getInterviewTime() != null) existing.setInterviewTime(updated.getInterviewTime());
//        if (updated.getMode() != null) existing.setMode(updated.getMode());
//        if (updated.getMeetingLink() != null) existing.setMeetingLink(updated.getMeetingLink());
//        if (updated.getRemarks() != null) existing.setRemarks(updated.getRemarks());
//        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
//
//        return interviewRepo.save(existing);
//    }
//
//    // =============================
//    // 🔐 Recruiter – ALL interviews
//    // =============================
//    public List<Interview> getMyInterviews() {
//        return interviewRepo.findByRecruiterId(AuthUtil.getUserId());
//    }
//
//    // =============================
//    // Candidate
//    // =============================
//    public List<Interview> getAllByCandidate(Long candidateId) {
//        return interviewRepo.findByCandidate_Id(candidateId);
//    }
//
//    // =============================
//    // Recruiter – Upcoming / Past
//    // =============================
//    public List<Interview> getUpcoming() {
//        LocalDate today = LocalDate.now();
//        return interviewRepo.findByRecruiterIdAndInterviewDateAfter(
//                AuthUtil.getUserId(),
//                today.minusDays(1)
//        );
//    }
//
//    public List<Interview> getPast() {
//        LocalDate today = LocalDate.now();
//        return interviewRepo.findByRecruiterIdAndInterviewDateBefore(
//                AuthUtil.getUserId(),
//                today
//        );
//    }
//
//    // =============================
//    // STATUS / CANCEL
//    // =============================
//    public Interview updateStatus(Long id, String status) {
//        Interview interview = interviewRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Interview not found"));
//        interview.setStatus(status.toUpperCase());
//        return interviewRepo.save(interview);
//    }
//
//    public void cancelInterview(Long id) {
//        Interview interview = interviewRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Interview not found"));
//        interview.setStatus("CANCELLED");
//        interviewRepo.save(interview);
//    }
//}

package com.ims.fullstack.service;

import com.ims.fullstack.dto.round.InterviewRequest;
import com.ims.fullstack.dto.round.InterviewResponse;
import com.ims.fullstack.model.Application;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.model.Interview;
import com.ims.fullstack.model.Job;
import com.ims.fullstack.model.enums.ApplicationStatus;
import com.ims.fullstack.model.enums.InterviewStatus;
import com.ims.fullstack.repository.ApplicationRepository;
import com.ims.fullstack.repository.CandidateRepository;
import com.ims.fullstack.repository.InterviewRepository;
import com.ims.fullstack.repository.JobRepository;
import com.ims.fullstack.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepo;
    private final CandidateRepository candidateRepo;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final MailService mailService;

    public InterviewResponse scheduleInterview(InterviewRequest request) {
        Candidate candidate = candidateRepo.findById(request.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Find and update the associated application to INTERVIEW_SCHEDULED
        Application application = applicationRepository
                .findByCandidate_IdAndJob_Id(candidate.getId(), job.getId())
                .orElseThrow(() -> new RuntimeException("No application found for this candidate and job"));
        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);

        Interview interview = Interview.builder()
                .recruiterId(AuthUtil.getUserId())
                .candidate(candidate)
                .job(job)
                .position(request.getPosition())
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .mode(request.getMode())
                .meetingLink(request.getMeetingLink())
                .remarks(request.getRemarks())
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview saved = interviewRepo.save(interview);

        try {
            mailService.sendInterviewInvite(
                    candidate.getEmail(),
                    "Interview Scheduled",
                    "Hi " + candidate.getFullName() + ",\n\nYour interview has been scheduled for "
                            + request.getInterviewDate() + " at " + request.getInterviewTime()
                            + ".\n\nRegards,\nRecruitment Team"
            );
        } catch (Exception ignored) {}

        return mapToResponse(saved);
    }

    public List<InterviewResponse> getRecruiterInterviews() {
        Long recruiterId = AuthUtil.getUserId();
        return interviewRepo.findByRecruiterId(recruiterId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InterviewResponse> getCandidateInterviews() {
        Long candidateId = AuthUtil.getUserId();
        return interviewRepo.findByCandidate_Id(candidateId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void cancelInterview(Long id) {
        Interview interview = interviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setStatus(InterviewStatus.CANCELLED);
        interviewRepo.save(interview);
    }

    private InterviewResponse mapToResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .recruiterId(interview.getRecruiterId())
                .candidateId(interview.getCandidate().getId())
                .candidateName(interview.getCandidate().getFullName())
                .candidateEmail(interview.getCandidate().getEmail())
                .jobId(interview.getJob() != null ? interview.getJob().getId() : null)
                .position(interview.getPosition())
                .interviewDate(interview.getInterviewDate())
                .interviewTime(interview.getInterviewTime())
                .mode(interview.getMode())
                .meetingLink(interview.getMeetingLink())
                .remarks(interview.getRemarks())
                .status(interview.getStatus())
                .resumeUrl(interview.getResumeUrl())
                .build();
    }
}