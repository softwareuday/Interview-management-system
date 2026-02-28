//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.AuthResponse;
//import com.ims.fullstack.dto.LoginRequest;
//import com.ims.fullstack.dto.recruiter.RecruiterRegisterRequest;
//import com.ims.fullstack.model.Recruiter;
//import com.ims.fullstack.model.enums.UserRole;
//import com.ims.fullstack.repository.RecruiterRepository;
//import com.ims.fullstack.security.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RecruiterAuthService {
//
//    private final RecruiterRepository recruiterRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//
//    public void registerRecruiter(RecruiterRegisterRequest request) {
//        if (recruiterRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already registered");
//        }
//
//        Recruiter recruiter = Recruiter.builder()
//                .fullName(request.getFullName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .phoneNumber(request.getPhoneNumber())
//                .companyName(request.getCompanyName())
//                .role(UserRole.RECRUITER)
//                .active(true)
//                .build();
//
//        recruiterRepository.save(recruiter);
//    }
//
//    public AuthResponse authenticateRecruiter(LoginRequest request) {
//        Recruiter recruiter = recruiterRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//
//        if (!passwordEncoder.matches(request.getPassword(), recruiter.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        String token = jwtService.generateToken(
//                recruiter.getEmail(),
//                recruiter.getId(),
//                recruiter.getRole().name()
//        );
//
//        return AuthResponse.builder()
//                .token(token)
//                .id(recruiter.getId())
//                .email(recruiter.getEmail())
//                .fullName(recruiter.getFullName()) // ⭐ CRITICAL FIX!
//                .role(recruiter.getRole().name())
//                .build();
//    }
//}


package com.ims.fullstack.service;

import com.ims.fullstack.dto.AuthResponse;
import com.ims.fullstack.dto.LoginRequest;
import com.ims.fullstack.dto.recruiter.RecruiterRegisterRequest;
import com.ims.fullstack.model.Recruiter;
import com.ims.fullstack.model.enums.UserRole;
import com.ims.fullstack.repository.RecruiterRepository;
import com.ims.fullstack.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterAuthService {
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
                .build();
        recruiterRepository.save(recruiter);
    }

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