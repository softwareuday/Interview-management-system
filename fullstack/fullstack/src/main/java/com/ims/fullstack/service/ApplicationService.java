//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.application.ApplyRequest;
//import com.ims.fullstack.dto.application.ApplicationResponse;
//import com.ims.fullstack.model.*;
//import com.ims.fullstack.model.enums.ApplicationStatus;
//import com.ims.fullstack.model.enums.JobStatus;
//import com.ims.fullstack.repository.*;
//import com.ims.fullstack.security.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ApplicationService {
//
//    private final ApplicationRepository applicationRepository;
//    private final CandidateRepository candidateRepository;
//    private final RecruiterRepository recruiterRepository;
//    private final JobRepository jobRepository;
//    private final FileStorageService fileStorageService;
//    private final JwtService jwtService;
//
//    // ===============================
//    // CANDIDATE APPLY FOR JOB
//    // ===============================
//    public ApplicationResponse applyForJob(
//            ApplyRequest request,
//            MultipartFile resume,
//            HttpServletRequest httpRequest
//    ) {
//
//        // 🔐 AUTH
//        String authHeader = httpRequest.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        String token = authHeader.substring(7);
//        String email = jwtService.extractUsername(token);
//        String role = jwtService.extractRole(token);
//
//        if (!"ROLE_CANDIDATE".equals(role)) {
//            throw new RuntimeException("Only candidates can apply for jobs");
//        }
//
//        // 👤 Candidate
//        Candidate candidate = candidateRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//
//        // 📄 Job
//        Job job = jobRepository.findById(request.getJobId())
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        if (job.getStatus() != JobStatus.OPEN) {
//            throw new RuntimeException("Cannot apply to a closed job");
//        }
//
//        // 📎 Resume validation
//        boolean hasProfileResume = candidate.getResumeUrl() != null;
//        boolean hasUploadedResume = resume != null && !resume.isEmpty();
//
//        if (!hasProfileResume && !hasUploadedResume) {
//            throw new RuntimeException("Resume is required to apply for this job");
//        }
//
//        // 🔁 Duplicate check
//        applicationRepository.findByCandidateAndJob(candidate, job)
//                .ifPresent(a -> {
//                    throw new RuntimeException("Already applied for this job");
//                });
//
//        // 📂 Resume storage
//        String resumeUrl = candidate.getResumeUrl();
//        if (hasUploadedResume) {
//            resumeUrl = fileStorageService.storeFile(resume);
//        }
//
//        // 📝 Save Application
//        Application application = Application.builder()
//                .candidate(candidate)
//                .job(job)
//                .recruiter(job.getRecruiter())
//                .resumeUrl(resumeUrl)
//                .coverLetter(request.getCoverLetter())
//                .status(ApplicationStatus.APPLIED)
//                .appliedAt(LocalDateTime.now())
//                .build();
//
//        Application saved = applicationRepository.save(application);
//
//        // ✅ RETURN DTO (CRITICAL FIX)
//        return ApplicationResponse.builder()
//                .applicationId(saved.getId())
//                .candidateId(candidate.getId())
//                .candidateName(candidate.getFullName())
//                .candidateEmail(candidate.getEmail())
//                .resumeUrl(saved.getResumeUrl())
//                .coverLetter(saved.getCoverLetter())
//                .status(saved.getStatus().name())
//                .appliedAt(saved.getAppliedAt())
//                .build();
//    }
//
//    // ===============================
//    // RECRUITER VIEW APPLICANTS
//    // ===============================
//    public List<ApplicationResponse> getApplicantsForRecruiter(
//            Long jobId,
//            HttpServletRequest request
//    ) {
//
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        String token = authHeader.substring(7);
//        String email = jwtService.extractUsername(token);
//        String role = jwtService.extractRole(token);
//
//        if (!"ROLE_RECRUITER".equals(role)) {
//            throw new RuntimeException("Only recruiters can view applicants");
//        }
//
//        Recruiter recruiter = recruiterRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
//
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
//            throw new RuntimeException("Not authorized to view applicants for this job");
//        }
//
//        return applicationRepository.findByJob_Id(jobId)
//                .stream()
//                .filter(app -> app.getStatus() != ApplicationStatus.REJECTED)
//                .map(app -> ApplicationResponse.builder()
//                        .applicationId(app.getId())
//                        .candidateId(app.getCandidate().getId())
//                        .candidateName(app.getCandidate().getFullName())
//                        .candidateEmail(app.getCandidate().getEmail())
//                        .resumeUrl(app.getResumeUrl())
//                        .coverLetter(app.getCoverLetter())
//                        .status(app.getStatus().name())
//                        .appliedAt(app.getAppliedAt())
//                        .build()
//                )
//                .toList();
//    }
//    public void updateStatus(Long applicationId, String status, HttpServletRequest request) {
//
//        String token = request.getHeader("Authorization").substring(7);
//        String email = jwtService.extractUsername(token);
//
//        Recruiter recruiter = recruiterRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
//
//        Application application = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        if (!application.getRecruiter().getId().equals(recruiter.getId())) {
//            throw new RuntimeException("Not authorized");
//        }
//
//        application.setStatus(ApplicationStatus.valueOf(status));
//        applicationRepository.save(application);
//    }
//
//}


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

    public ApplicationResponse applyForJob(ApplyRequest request, MultipartFile resume) {
        // Get authenticated user from SecurityContext
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new RuntimeException("Unauthorized");
        }

        // Verify role
        if (!user.getRole().equals("ROLE_CANDIDATE")) {
            throw new RuntimeException("Only candidates can apply for jobs");
        }

        Candidate candidate = candidateRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!"OPEN".equals(job.getStatus())) {
            throw new RuntimeException("Cannot apply to a closed job");
        }

        boolean hasProfileResume = candidate.getResumeUrl() != null;
        boolean hasUploadedResume = resume != null && !resume.isEmpty();
        if (!hasProfileResume && !hasUploadedResume) {
            throw new RuntimeException("Resume is required to apply for this job");
        }

        applicationRepository.findByCandidateAndJob(candidate, job)
                .ifPresent(a -> { throw new RuntimeException("Already applied for this job"); });

        String resumeUrl = candidate.getResumeUrl();
        if (hasUploadedResume) {
            resumeUrl = fileStorageService.storeFile(resume);
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

    private ApplicationResponse mapToResponse(Application app) {
        return ApplicationResponse.builder()
                .applicationId(app.getId())
                .candidateId(app.getCandidate().getId())
                .candidateName(app.getCandidate().getFullName())
                .candidateEmail(app.getCandidate().getEmail())
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