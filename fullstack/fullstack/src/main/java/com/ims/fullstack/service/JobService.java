//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.recruiter.JobRequest;
//import com.ims.fullstack.dto.recruiter.JobResponse;
//import com.ims.fullstack.model.Job;
//import com.ims.fullstack.model.enums.JobStatus;
//import com.ims.fullstack.repository.ApplicationRepository;
//import com.ims.fullstack.repository.JobRepository;
//import com.ims.fullstack.repository.RecruiterRepository;
//import com.ims.fullstack.security.AuthUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class JobService {
//
//    private final JobRepository jobRepository;
//    private final RecruiterRepository recruiterRepository;
//    private final ApplicationRepository applicationRepository;
//
//    @Transactional
//    public JobResponse createJob(JobRequest request) {
//        Long recruiterId = AuthUtil.getUserId();
//
//        var recruiter = recruiterRepository.findById(recruiterId)
//                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
//
//        Job job = Job.builder()
//                .title(request.getTitle())
//                .description(request.getDescription())
//                .department(request.getDepartment())
//                .location(request.getLocation())
//                .salaryRange(request.getSalaryRange())
//                .experienceRequired(request.getExperienceRequired())
//                .requiredSkills(request.getRequiredSkills()) // ⭐ NEW
//                .jobType(request.getJobType()) // ⭐ NEW
//                .lastDateToApply(request.getLastDateToApply()) // ⭐ NEW
//                .status(JobStatus.OPEN)
//                .recruiter(recruiter)
//                .build();
//
//        job = jobRepository.save(job);
//
//        return mapToResponse(job, null);
//    }
//
//    public List<JobResponse> getRecruiterJobs() {
//        Long recruiterId = AuthUtil.getUserId();
//        List<Job> jobs = jobRepository.findByRecruiter_Id(recruiterId);
//
//        return jobs.stream()
//                .map(job -> mapToResponse(job, null))
//                .collect(Collectors.toList());
//    }
//
//    public JobResponse getJobById(Long jobId) {
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        // Check if current user applied (if authenticated as candidate)
//        Boolean hasApplied = false;
//        try {
//            Long candidateId = AuthUtil.getUserId();
//            hasApplied = applicationRepository.findByCandidate_IdAndJob_Id(candidateId, jobId).isPresent();
//        } catch (Exception e) {
//            // Not authenticated or not a candidate
//            hasApplied = false;
//        }
//
//        return mapToResponse(job, hasApplied);
//    }
//
//    @Transactional
//    public JobResponse updateJob(Long jobId, JobRequest request) {
//        Long recruiterId = AuthUtil.getUserId();
//
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        // Verify ownership
//        if (!job.getRecruiter().getId().equals(recruiterId)) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        job.setTitle(request.getTitle());
//        job.setDescription(request.getDescription());
//        job.setDepartment(request.getDepartment());
//        job.setLocation(request.getLocation());
//        job.setSalaryRange(request.getSalaryRange());
//        job.setExperienceRequired(request.getExperienceRequired());
//        job.setRequiredSkills(request.getRequiredSkills());
//        job.setJobType(request.getJobType());
//        job.setLastDateToApply(request.getLastDateToApply());
//
//        job = jobRepository.save(job);
//
//        return mapToResponse(job, null);
//    }
//
//    @Transactional
//    public void closeJob(Long jobId) {
//        Long recruiterId = AuthUtil.getUserId();
//
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        // Verify ownership
//        if (!job.getRecruiter().getId().equals(recruiterId)) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        job.setStatus(JobStatus.CLOSED);
//        jobRepository.save(job);
//    }
//
//    @Transactional
//    public void deleteJob(Long jobId) {
//        Long recruiterId = AuthUtil.getUserId();
//
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        // Verify ownership
//        if (!job.getRecruiter().getId().equals(recruiterId)) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        jobRepository.delete(job);
//    }
//
//    // ⭐ CRITICAL: Map to response with all fields
//    private JobResponse mapToResponse(Job job, Boolean hasApplied) {
//        // Calculate applicants count
//        long applicantsCount = applicationRepository.countByJob_Id(job.getId());
//
//        // Parse skills from comma-separated to list
//        List<String> skillsList = parseSkills(job.getRequiredSkills());
//
//        return JobResponse.builder()
//                .id(job.getId())
//                .title(job.getTitle())
//                .description(job.getDescription())
//                .department(job.getDepartment())
//                .location(job.getLocation())
//                .salaryRange(job.getSalaryRange())
//                .experienceRequired(job.getExperienceRequired())
//                .status(job.getStatus())
//                .companyName(job.getRecruiter().getCompanyName())
//                .applicantsCount((int) applicantsCount) // ⭐ CALCULATED!
//                .hasApplied(hasApplied) // ⭐ CALCULATED!
//                .requiredSkills(skillsList) // ⭐ PARSED ARRAY!
//                .jobType(job.getJobType())
//                .createdAt(job.getCreatedAt())
//                .updatedAt(job.getUpdatedAt())
//                .lastDateToApply(job.getLastDateToApply())
//                .build();
//    }
//
//    private List<String> parseSkills(String skills) {
//        if (skills == null || skills.trim().isEmpty()) {
//            return List.of();
//        }
//        return Arrays.stream(skills.split(","))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .collect(Collectors.toList());
//    }
//}

package com.ims.fullstack.service;

import com.ims.fullstack.dto.recruiter.JobRequest;
import com.ims.fullstack.dto.recruiter.JobResponse;
import com.ims.fullstack.model.Job;
import com.ims.fullstack.repository.ApplicationRepository;
import com.ims.fullstack.repository.JobRepository;
import com.ims.fullstack.repository.RecruiterRepository;
import com.ims.fullstack.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public JobResponse createJob(JobRequest request) {
        Long recruiterId = AuthUtil.getUserId();
        var recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .department(request.getDepartment())
                .location(request.getLocation())
                .salaryRange(request.getSalaryRange())
                .experienceRequired(request.getExperienceRequired())
                .requiredSkills(request.getRequiredSkills())
                .jobType(request.getJobType())
                .lastDateToApply(request.getLastDateToApply())
                .status("OPEN")                     // ⭐ String literal
                .recruiter(recruiter)
                .build();

        job = jobRepository.save(job);
        return mapToResponse(job, null);
    }

    public List<JobResponse> getMyJobs() {
        Long recruiterId = AuthUtil.getUserId();
        return jobRepository.findByRecruiter_Id(recruiterId).stream()
                .map(job -> mapToResponse(job, null))
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(Long jobId, Long candidateId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        boolean hasApplied = candidateId != null &&
                applicationRepository.findByCandidate_IdAndJob_Id(candidateId, jobId).isPresent();

        return mapToResponse(job, hasApplied);
    }

    @Transactional
    public JobResponse updateJob(Long jobId, JobRequest request) {
        Long recruiterId = AuthUtil.getUserId();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("Unauthorized");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setDepartment(request.getDepartment());
        job.setLocation(request.getLocation());
        job.setSalaryRange(request.getSalaryRange());
        job.setExperienceRequired(request.getExperienceRequired());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setJobType(request.getJobType());
        job.setLastDateToApply(request.getLastDateToApply());

        job = jobRepository.save(job);
        return mapToResponse(job, null);
    }

    @Transactional
    public void closeJob(Long jobId) {
        Long recruiterId = AuthUtil.getUserId();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("Unauthorized");
        }

        job.setStatus("CLOSED");                    // ⭐ String literal
        jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId) {
        Long recruiterId = AuthUtil.getUserId();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("Unauthorized");
        }

        jobRepository.delete(job);
    }

    private JobResponse mapToResponse(Job job, Boolean hasApplied) {
        long applicantsCount = applicationRepository.countByJob_Id(job.getId());
        List<String> skillsList = parseSkills(job.getRequiredSkills());

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .department(job.getDepartment())
                .location(job.getLocation())
                .salaryRange(job.getSalaryRange())
                .experienceRequired(job.getExperienceRequired())
                .status(job.getStatus())            // ⭐ now returns String directly
                .companyName(job.getRecruiter().getCompanyName())
                .applicantsCount((int) applicantsCount)
                .hasApplied(hasApplied)
                .requiredSkills(skillsList)
                .jobType(job.getJobType())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .lastDateToApply(job.getLastDateToApply())
                .build();
    }

    private List<String> parseSkills(String skills) {
        if (skills == null || skills.trim().isEmpty()) return List.of();
        return Arrays.stream(skills.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}