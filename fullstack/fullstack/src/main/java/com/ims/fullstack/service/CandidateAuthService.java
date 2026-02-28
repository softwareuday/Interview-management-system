//package com.ims.fullstack.service;
//
//import com.ims.fullstack.dto.AuthResponse;
//import com.ims.fullstack.dto.LoginRequest;
//import com.ims.fullstack.dto.candidate.CandidateRegisterRequest;
//import com.ims.fullstack.model.Candidate;
//import com.ims.fullstack.model.enums.UserRole;
//import com.ims.fullstack.repository.CandidateRepository;
//import com.ims.fullstack.security.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CandidateAuthService {
//
//    private final CandidateRepository candidateRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//
//    public void registerCandidate(CandidateRegisterRequest request) {
//        if (candidateRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already registered");
//        }
//
//        Candidate candidate = Candidate.builder()
//                .fullName(request.getFullName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(UserRole.CANDIDATE)
//                .build();
//
//        candidateRepository.save(candidate);
//    }
//
//    public AuthResponse authenticateCandidate(LoginRequest request) {
//        Candidate candidate = candidateRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//
//        if (!passwordEncoder.matches(request.getPassword(), candidate.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        String token = jwtService.generateToken(
//                candidate.getEmail(),
//                candidate.getId(),
//                candidate.getRole().name()
//        );
//
//        return AuthResponse.builder()
//                .token(token)
//                .id(candidate.getId())
//                .email(candidate.getEmail())
//                .fullName(candidate.getFullName()) // ⭐ CRITICAL FIX!
//                .role(candidate.getRole().name())
//                .build();
//    }
//}

package com.ims.fullstack.service;

import com.ims.fullstack.dto.AuthResponse;
import com.ims.fullstack.dto.LoginRequest;
import com.ims.fullstack.dto.candidate.CandidateRegisterRequest;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.model.enums.UserRole;
import com.ims.fullstack.repository.CandidateRepository;
import com.ims.fullstack.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateAuthService {
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(CandidateRegisterRequest request) {
        if (candidateRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        Candidate candidate = Candidate.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CANDIDATE)
                .build();
        candidateRepository.save(candidate);
    }

    public AuthResponse login(LoginRequest request) {
        Candidate candidate = candidateRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), candidate.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtService.generateToken(candidate.getEmail(), candidate.getId(), candidate.getRole().name());
        return AuthResponse.builder()
                .token(token)
                .id(candidate.getId())
                .email(candidate.getEmail())
                .fullName(candidate.getFullName())
                .role(candidate.getRole().name())
                .build();
    }
}