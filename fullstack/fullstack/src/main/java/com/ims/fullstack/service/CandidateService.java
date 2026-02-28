////package com.ims.fullstack.service;
////
////import com.ims.fullstack.model.Candidate;
////import com.ims.fullstack.repository.CandidateRepository;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////
////import java.time.Instant;
////import java.util.Optional;
////
////@Service
////@RequiredArgsConstructor
////public class CandidateService {
////
////    private final CandidateRepository candidateRepository;
////
////    /**
////     * Find a candidate by email or create a new "external" candidate (created by recruiter).
////     * Sets isExternal=true and lastUpdatedBy when creating new candidate.
////     *
////     * @param candidateFromRequest Candidate object coming from frontend (may contain fullName, email, phone)
////     * @param recruiterIdentifier  who created/updated this candidate (email or id string)
////     * @return existing or newly created Candidate
////     */
////    public Candidate findOrCreateExternalCandidate(Candidate candidateFromRequest, String recruiterIdentifier) {
////        Optional<Candidate> existing = candidateRepository.findByEmail(candidateFromRequest.getEmail());
////        if (existing.isPresent()) {
////            Candidate c = existing.get();
////            // update lastUpdatedBy if recruiter is scheduling
////            c.setLastUpdatedBy(recruiterIdentifier);
////            return candidateRepository.save(c);
////        }
////
////        Candidate newCand = Candidate.builder()
////                .fullName(candidateFromRequest.getFullName())
////                .email(candidateFromRequest.getEmail())
////                .phone(candidateFromRequest.getPhone())
////                // optional fields - keep defaults or null
////                .experience(candidateFromRequest.getExperience() != null ? candidateFromRequest.getExperience() : "")
////                .skills(candidateFromRequest.getSkills() != null ? candidateFromRequest.getSkills() : "")
////                // password might not be set for external candidate
////                .password(candidateFromRequest.getPassword() != null ? candidateFromRequest.getPassword() : "")
////                .role("ROLE_CANDIDATE")
////                .createdAt(Instant.now())
////                .isExternal(true)
////                .lastUpdatedBy(recruiterIdentifier)
////                .build();
////
////        return candidateRepository.save(newCand);
////    }
////
////    public Candidate findByEmail(String email) {
////        return candidateRepository.findByEmail(email).orElse(null);
////    }
////
////    public Candidate findById(Long id) {
////        return candidateRepository.findById(id).orElse(null);
////    }
////}
//package com.ims.fullstack.service;
//
//import com.ims.fullstack.model.Candidate;
//import com.ims.fullstack.repository.CandidateRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CandidateService {
//
//    private final CandidateRepository candidateRepository;
//
//    // 🔹 Used by JWT-based flows
//    public Candidate findByEmail(String email) {
//        return candidateRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//    }
//
//    // 🔹 Used by Interview / Application / Round flows
//    public Candidate findById(Long id) {
//        return candidateRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//    }
//
//    // 🔹 Resume update only (NO audit fields)
//    public Candidate updateResume(Long candidateId, String resumeUrl) {
//        Candidate c = findById(candidateId);
//        c.setResumeUrl(resumeUrl);
//        return candidateRepository.save(c);
//    }
//}
package com.ims.fullstack.service;

import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public Candidate findByEmail(String email) {
        return candidateRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

    public Candidate findById(Long id) {
        return candidateRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

    public Candidate updateResume(Long candidateId, String resumeUrl) {
        Candidate c = findById(candidateId);
        c.setResumeUrl(resumeUrl);
        return candidateRepository.save(c);
    }
}