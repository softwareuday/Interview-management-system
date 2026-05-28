
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