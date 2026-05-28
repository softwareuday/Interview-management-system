package com.ims.fullstack.service;

import com.ims.fullstack.dto.application.ApplyRequest;
import com.ims.fullstack.dto.application.ApplicationResponse;
import com.ims.fullstack.model.*;
import com.ims.fullstack.model.enums.ApplicationStatus;
import com.ims.fullstack.repository.*;
import com.ims.fullstack.security.AuthUtil;
import com.ims.fullstack.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public ApplicationResponse applyForJob(ApplyRequest request, MultipartFile resume) {
        // Get authenticated user
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser;

        Candidate candidate = null;
        String resumeUrl = null;

        // First get the job
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!"OPEN".equals(job.getStatus())) {
            throw new RuntimeException("Cannot apply to a closed job");
        }

        if (isAuthenticated) {
            AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
            if (!user.getRole().equals("ROLE_CANDIDATE")) {
                throw new RuntimeException("Only candidates can apply for jobs");
            }
            candidate = candidateRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            // Check if candidate already applied
            applicationRepository.findByCandidateAndJob(candidate, job)
                    .ifPresent(a -> { throw new RuntimeException("Already applied for this job"); });

            // Use candidate's resume if available
            resumeUrl = candidate.getResumeUrl();
        } else {
            // Guest application - must provide name and email
            if (request.getGuestName() == null || request.getGuestName().trim().isEmpty() ||
                    request.getGuestEmail() == null || request.getGuestEmail().trim().isEmpty()) {
                throw new RuntimeException("Name and email are required for guest applications");
            }
        }

        // If resume uploaded in this request, use it
        if (resume != null && !resume.isEmpty()) {
            resumeUrl = fileStorageService.storeFile(resume);
        } else if (candidate == null && resumeUrl == null) {
            throw new RuntimeException("Resume is required to apply for this job");
        }

        Application application = Application.builder()
                .candidate(candidate)
                .job(job)
                .recruiter(job.getRecruiter())
                .resumeUrl(resumeUrl)
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();

        // Set guest fields if candidate is null
        if (candidate == null) {
            application.setGuestName(request.getGuestName());
            application.setGuestEmail(request.getGuestEmail());
        }

        Application saved = applicationRepository.save(application);
        return mapToResponse(saved);
    }

    public List<ApplicationResponse> getCandidateApplications() {
        AuthenticatedUser user = AuthUtil.getCurrentUser();
        if (user == null || !user.getRole().equals("ROLE_CANDIDATE")) {
            throw new RuntimeException("Unauthorized");
        }

        Candidate candidate = candidateRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        return applicationRepository.findByCandidate_Id(candidate.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getRecruiterApplications() {
        AuthenticatedUser user = AuthUtil.getCurrentUser();
        if (user == null || !user.getRole().equals("ROLE_RECRUITER")) {
            throw new RuntimeException("Unauthorized");
        }

        Recruiter recruiter = recruiterRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return applicationRepository.findAll().stream()
                .filter(app -> app.getRecruiter().getId().equals(recruiter.getId()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getJobApplications(Long jobId) {
        AuthenticatedUser user = AuthUtil.getCurrentUser();
        if (user == null || !user.getRole().equals("ROLE_RECRUITER")) {
            throw new RuntimeException("Unauthorized");
        }

        Recruiter recruiter = recruiterRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("Not authorized to view applications for this job");
        }

        return applicationRepository.findByJob_Id(jobId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStatus(Long applicationId, String status) {
        AuthenticatedUser user = AuthUtil.getCurrentUser();
        if (user == null || !user.getRole().equals("ROLE_RECRUITER")) {
            throw new RuntimeException("Unauthorized");
        }

        Recruiter recruiter = recruiterRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("Not authorized");
        }

        application.setStatus(ApplicationStatus.valueOf(status));
        applicationRepository.save(application);
    }

    // ========== NEW METHODS FOR RECRUITER ATS ==========
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    public void save(Application application) {
        applicationRepository.save(application);
    }
    // ====================================================

    private ApplicationResponse mapToResponse(Application app) {
        return ApplicationResponse.builder()
                .applicationId(app.getId())
                .candidateId(app.getCandidate() != null ? app.getCandidate().getId() : null)
                .candidateName(app.getCandidate() != null ? app.getCandidate().getFullName() : app.getGuestName())
                .candidateEmail(app.getCandidate() != null ? app.getCandidate().getEmail() : app.getGuestEmail())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .resumeUrl(app.getResumeUrl())
                .coverLetter(app.getCoverLetter())
                .status(app.getStatus())
                .appliedAt(app.getAppliedAt())
                .atsScore(app.getAtsScore())
                .matchedKeywords(app.getMatchedKeywords())
                .missingKeywords(app.getMissingKeywords())
                .build();
    }
}