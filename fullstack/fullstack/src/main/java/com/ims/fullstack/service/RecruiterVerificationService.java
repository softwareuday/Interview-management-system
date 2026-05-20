package com.ims.fullstack.service;

import com.ims.fullstack.model.Recruiter;
import com.ims.fullstack.model.enums.VerificationStatus;
import com.ims.fullstack.repository.RecruiterRepository;
import com.ims.fullstack.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterVerificationService {

    private final RecruiterRepository recruiterRepository;

    public Recruiter getVerifiedRecruiter() {
        Long recruiterId = AuthUtil.getUserId();
        if (recruiterId == null) {
            throw new RuntimeException("Recruiter not authenticated");
        }
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        if (recruiter.getVerificationStatus() == VerificationStatus.UNVERIFIED) {
            throw new RuntimeException("Your account is not verified. Please complete company verification to access this feature.");
        }
        // For now, both VERIFIED and PREMIUM allowed
        return recruiter;
    }
}