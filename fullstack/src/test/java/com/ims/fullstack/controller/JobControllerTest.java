package com.ims.fullstack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.fullstack.dto.recruiter.JobRequest;
import com.ims.fullstack.dto.recruiter.JobResponse;
import com.ims.fullstack.model.enums.JobType;
import com.ims.fullstack.security.JwtService;
import com.ims.fullstack.service.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
@Import(JobControllerTest.TestSecurityConfig.class)  // Import custom security config
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @MockBean
    private JwtService jwtService; // still needed because security filters reference it

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())          // disable CSRF for tests
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/recruiter/jobs").permitAll() // or .authenticated() and use @WithMockUser
                            .anyRequest().authenticated()
                    );
            return http.build();
        }
    }

    @Test
    void createJob_ShouldReturnCreatedJob() throws Exception {
        JobRequest request = JobRequest.builder()
                .title("DevOps Engineer")
                .description("AWS, Kubernetes")
                .location("Hybrid")
                .jobType(JobType.FULL_TIME)
                .build();

        JobResponse response = JobResponse.builder()
                .id(5L)
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .status("OPEN")
                .companyName("TechCorp")
                .applicantsCount(0)
                .build();

        when(jobService.createJob(any(JobRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/recruiter/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("DevOps Engineer"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    // other tests...
}