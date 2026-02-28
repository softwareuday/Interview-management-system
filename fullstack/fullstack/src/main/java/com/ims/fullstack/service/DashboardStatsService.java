//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.dashboard.DashboardActivityResponse;
//import com.ims.fullstack.dto.dashboard.DashboardStatsResponse;
//import com.ims.fullstack.model.enums.JobStatus;
//import com.ims.fullstack.repository.*;
//import com.ims.fullstack.security.AuthUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class DashboardStatsService {
//
//    private final JobRepository jobRepository;
//    private final ApplicationRepository applicationRepository;
//    private final InterviewRepository interviewRepository;
//
//    // =============================
//    // DASHBOARD STATS
//    // =============================
//    public DashboardStatsResponse getStats() {
//
//        Long recruiterId = AuthUtil.getUserId();
//
//        long totalJobs = jobRepository.findByRecruiter_Id(recruiterId).size();
//        long openJobs = jobRepository.findByRecruiter_Id(recruiterId)
//                .stream()
//                .filter(j -> j.getStatus() == JobStatus.OPEN)
//                .count();
//
//        long totalApplications = applicationRepository
//                .findAll()
//                .stream()
//                .filter(a -> a.getRecruiter().getId().equals(recruiterId))
//                .count();
//
//        long totalInterviews = interviewRepository
//                .findByRecruiterId(recruiterId)
//                .size();
//
//        return DashboardStatsResponse.builder()
//                .totalJobs(totalJobs)
//                .openJobs(openJobs)
//                .totalApplications(totalApplications)
//                .totalInterviews(totalInterviews)
//                .build();
//    }
//
//    // =============================
//    // DASHBOARD ACTIVITIES (SIMPLE)
//    // =============================
//    public List<DashboardActivityResponse> getRecentActivities() {
//
//        Long recruiterId = AuthUtil.getUserId();
//        List<DashboardActivityResponse> activities = new ArrayList<>();
//
//        // Recent Jobs
//        jobRepository.findByRecruiter_Id(recruiterId).stream()
//                .limit(5)
//                .forEach(job ->
//                        activities.add(
//                                DashboardActivityResponse.builder()
//                                        .type("JOB")
//                                        .description("Job created: " + job.getTitle())
//                                        .time(job.getCreatedAt())
//                                        .build()
//                        )
//                );
//
//        // Recent Interviews
//        interviewRepository.findByRecruiterId(recruiterId).stream()
//                .limit(5)
//                .forEach(interview ->
//                        activities.add(
//                                DashboardActivityResponse.builder()
//                                        .type("INTERVIEW")
//                                        .description("Interview scheduled for " + interview.getPosition())
//                                        .time(interview.getInterviewDate().atStartOfDay())
//                                        .build()
//                        )
//                );
//
//        return activities;
//    }
//}
package com.ims.fullstack.service;

import com.ims.fullstack.dto.dashboard.DashboardActivityResponse;
import com.ims.fullstack.dto.dashboard.DashboardStatsResponse;
import com.ims.fullstack.repository.*;
import com.ims.fullstack.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardStatsService {
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public DashboardStatsResponse getStats() {
        Long recruiterId = AuthUtil.getUserId();
        long totalJobs = jobRepository.findByRecruiter_Id(recruiterId).size();
        long openJobs = jobRepository.findByRecruiter_Id(recruiterId).stream()
                .filter(j -> "OPEN".equals(j.getStatus()))   // ⭐ String comparison
                .count();
        long totalApplications = applicationRepository.findAll().stream()
                .filter(a -> a.getRecruiter().getId().equals(recruiterId)).count();
        long totalInterviews = interviewRepository.findByRecruiterId(recruiterId).size();
        return DashboardStatsResponse.builder()
                .totalJobs(totalJobs)
                .openJobs(openJobs)
                .totalApplications(totalApplications)
                .totalInterviews(totalInterviews)
                .build();
    }

    public List<DashboardActivityResponse> getRecentActivities() {
        Long recruiterId = AuthUtil.getUserId();
        List<DashboardActivityResponse> activities = new ArrayList<>();
        jobRepository.findByRecruiter_Id(recruiterId).stream().limit(5).forEach(job ->
                activities.add(DashboardActivityResponse.builder()
                        .type("JOB")
                        .description("Job created: " + job.getTitle())
                        .time(job.getCreatedAt())
                        .build())
        );
        interviewRepository.findByRecruiterId(recruiterId).stream().limit(5).forEach(interview ->
                activities.add(DashboardActivityResponse.builder()
                        .type("INTERVIEW")
                        .description("Interview scheduled for " + interview.getPosition())
                        .time(interview.getInterviewDate().atStartOfDay())
                        .build())
        );
        return activities;
    }
}