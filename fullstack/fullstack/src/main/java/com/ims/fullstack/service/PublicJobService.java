


package com.ims.fullstack.service;

import com.ims.fullstack.dto.recruiter.JobResponse;
import com.ims.fullstack.model.Job;
import com.ims.fullstack.repository.ApplicationRepository;
import com.ims.fullstack.repository.JobRepository;
import com.ims.fullstack.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicJobService {
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final JwtService jwtService;

    public Page<JobResponse> getPublicJobs(int page, int size, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobsPage = jobRepository.findByStatus("OPEN", pageable);

        Long candidateId = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            candidateId = jwtService.extractId(token);
        }
        Long finalCandidateId = candidateId;

        return jobsPage.map(job -> {
            boolean hasApplied = finalCandidateId != null &&
                    applicationRepository.findByCandidate_IdAndJob_Id(finalCandidateId, job.getId()).isPresent();
            return JobResponse.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .location(job.getLocation())
                    .salaryRange(job.getSalaryRange())
                    .status(job.getStatus())
                    .companyName(job.getRecruiter().getCompanyName())
                    .hasApplied(hasApplied)
                    .build();
        });
    }
}