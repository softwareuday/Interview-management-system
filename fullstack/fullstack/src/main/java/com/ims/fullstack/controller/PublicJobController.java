package com.ims.fullstack.controller;

import com.ims.fullstack.dto.recruiter.JobResponse;
import com.ims.fullstack.service.PublicJobService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public/jobs")
@RequiredArgsConstructor
public class PublicJobController {
    private final PublicJobService publicJobService;

    @GetMapping
    public ResponseEntity<List<JobResponse>> browseJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Page<JobResponse> pageResult = publicJobService.getPublicJobs(page, size, request);
        return ResponseEntity.ok(pageResult.getContent());
    }
}