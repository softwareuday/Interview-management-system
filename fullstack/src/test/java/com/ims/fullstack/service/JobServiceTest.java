package com.ims.fullstack.service;

import com.ims.fullstack.dto.recruiter.JobRequest;
import com.ims.fullstack.dto.recruiter.JobResponse;
import com.ims.fullstack.model.Job;
import com.ims.fullstack.model.Recruiter;
import com.ims.fullstack.model.enums.JobType;
import com.ims.fullstack.repository.ApplicationRepository;
import com.ims.fullstack.repository.JobRepository;
import com.ims.fullstack.repository.RecruiterRepository;
import com.ims.fullstack.security.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private JobService jobService;

    private Recruiter testRecruiter;
    private JobRequest validRequest;

    @BeforeEach
    void setUp() {
        testRecruiter = Recruiter.builder()
                .id(1L)
                .fullName("Test Recruiter")
                .email("recruiter@test.com")
                .companyName("Test Company")
                .build();

        validRequest = JobRequest.builder()
                .title("Software Engineer")
                .description("Great job")
                .location("Remote")
                .jobType(JobType.FULL_TIME)
                .build();
    }

    @Test
    void createJob_Success() {
        // Mock static AuthUtil.getUserId()
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(AuthUtil::getUserId).thenReturn(1L);
            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(testRecruiter));

            Job savedJob = Job.builder()
                    .id(10L)
                    .title(validRequest.getTitle())
                    .description(validRequest.getDescription())
                    .location(validRequest.getLocation())
                    .jobType(validRequest.getJobType())
                    .recruiter(testRecruiter)
                    .status("OPEN")
                    .build();

            when(jobRepository.save(any(Job.class))).thenReturn(savedJob);
            when(applicationRepository.countByJob_Id(anyLong())).thenReturn(0L);

            JobResponse response = jobService.createJob(validRequest);

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(10L);
            assertThat(response.getTitle()).isEqualTo("Software Engineer");
            assertThat(response.getCompanyName()).isEqualTo("Test Company");
        }
    }

    @Test
    void createJob_RecruiterNotFound_ThrowsException() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(AuthUtil::getUserId).thenReturn(99L);
            when(recruiterRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> jobService.createJob(validRequest));
        }
    }
}