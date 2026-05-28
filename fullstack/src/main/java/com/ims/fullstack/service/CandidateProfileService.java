package com.ims.fullstack.service;

import com.ims.fullstack.dto.candidate.CandidateProfileResponse;
import com.ims.fullstack.dto.candidate.CandidateProfileUpdateRequest;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.repository.CandidateRepository;
import com.ims.fullstack.security.AuthUtil;
import com.ims.fullstack.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CandidateProfileService {

    private final CandidateRepository candidateRepository;

    public CandidateProfileResponse getProfile() {
        AuthenticatedUser user = AuthUtil.getCurrentUser();
        if (user == null || !user.getRole().equals("ROLE_CANDIDATE")) {
            throw new RuntimeException("Unauthorized");
        }

        Candidate candidate = candidateRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        return mapToResponse(candidate);
    }

    @Transactional
    public CandidateProfileResponse updateProfile(CandidateProfileUpdateRequest request) {
        AuthenticatedUser user = AuthUtil.getCurrentUser();
        if (user == null || !user.getRole().equals("ROLE_CANDIDATE")) {
            throw new RuntimeException("Unauthorized");
        }

        Candidate candidate = candidateRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        if (request.getPhoneNumber() != null) {
            candidate.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getLocation() != null) {
            candidate.setLocation(request.getLocation());
        }
        if (request.getLinkedinUrl() != null) {
            candidate.setLinkedinUrl(request.getLinkedinUrl());
        }
        if (request.getSkills() != null) {
            candidate.setSkills(request.getSkills());
        }

        candidate = candidateRepository.save(candidate);
        return mapToResponse(candidate);
    }

    private CandidateProfileResponse mapToResponse(Candidate candidate) {
        return CandidateProfileResponse.builder()
                .id(candidate.getId())
                .fullName(candidate.getFullName())
                .email(candidate.getEmail())
                .phoneNumber(candidate.getPhoneNumber())
                .location(candidate.getLocation())
                .linkedinUrl(candidate.getLinkedinUrl())
                .skills(candidate.getSkills())
                .resumeUrl(candidate.getResumeUrl())
                .build();
    }
}