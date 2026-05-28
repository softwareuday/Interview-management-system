

package com.ims.fullstack.service;

import com.ims.fullstack.dto.AuthResponse;
import com.ims.fullstack.dto.LoginRequest;
import com.ims.fullstack.dto.recruiter.RecruiterRegisterRequest;
import com.ims.fullstack.dto.recruiter.RecruiterStep1Request;
import com.ims.fullstack.model.Recruiter;
import com.ims.fullstack.model.enums.UserRole;
import com.ims.fullstack.model.enums.VerificationStatus;
import com.ims.fullstack.repository.RecruiterRepository;
import com.ims.fullstack.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecruiterAuthService {
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    // Existing register method (kept for backward compatibility)
    public void register(RecruiterRegisterRequest request) {
        if (recruiterRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        Recruiter recruiter = Recruiter.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .role(UserRole.RECRUITER)
                .active(true)
                .verificationStatus(VerificationStatus.UNVERIFIED)
                .registrationStep(1)
                .isEmailVerified(false)
                .build();
        recruiterRepository.save(recruiter);
    }

    // New Step 1 registration
    @Transactional
    public void registerStep1(RecruiterStep1Request request) {
        if (recruiterRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        String otp = otpService.generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        Recruiter recruiter = Recruiter.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .role(UserRole.RECRUITER)
                .active(true)
                .verificationStatus(VerificationStatus.UNVERIFIED)
                .registrationStep(1)
                .isEmailVerified(false)
                .otpCode(otp)
                .otpExpiry(expiry)
                .build();
        recruiterRepository.save(recruiter);
        otpService.sendOtpEmail(request.getEmail(), otp);
    }

    @Transactional
    public void verifyOtp(String email, String otpCode) {
        Recruiter recruiter = recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        if (recruiter.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }
        if (!otpService.isOtpValid(recruiter.getOtpCode(), recruiter.getOtpExpiry())) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        if (!recruiter.getOtpCode().equals(otpCode)) {
            throw new RuntimeException("Invalid OTP");
        }
        recruiter.setEmailVerified(true);
        recruiter.setOtpCode(null);
        recruiter.setOtpExpiry(null);
        recruiter.setRegistrationStep(2);
        recruiterRepository.save(recruiter);
    }

    // Existing login (unchanged)
    public AuthResponse login(LoginRequest request) {
        Recruiter recruiter = recruiterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), recruiter.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtService.generateToken(recruiter.getEmail(), recruiter.getId(), recruiter.getRole().name());
        return AuthResponse.builder()
                .token(token)
                .id(recruiter.getId())
                .email(recruiter.getEmail())
                .fullName(recruiter.getFullName())
                .role(recruiter.getRole().name())
                .build();
    }
}