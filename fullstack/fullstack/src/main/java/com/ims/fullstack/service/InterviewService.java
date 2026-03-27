

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