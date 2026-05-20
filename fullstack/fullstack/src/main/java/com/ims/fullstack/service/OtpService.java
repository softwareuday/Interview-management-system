package com.ims.fullstack.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final MailService mailService;

    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtpEmail(String to, String otp) {
        String subject = "Your Verification Code";
        String body = "Your OTP for verification is: " + otp + "\n\nThis code expires in 10 minutes.";
        mailService.sendInterviewInvite(to, subject, body);
    }

    public boolean isOtpValid(String storedOtp, LocalDateTime expiry) {
        if (storedOtp == null || expiry == null) return false;
        return LocalDateTime.now().isBefore(expiry);
    }
}