
package com.ims.fullstack.service;

import com.ims.fullstack.dto.candidate.ResumeUploadResponse;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateResumeService {
    private final CandidateRepository candidateRepository;
    private static final String BASE_UPLOAD_DIR = "uploads/resumes";

    public ResumeUploadResponse uploadResume(Long candidateId, MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("Resume file is empty");
        if (!"application/pdf".equalsIgnoreCase(file.getContentType()))
            throw new RuntimeException("Only PDF resumes are allowed");
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path candidateDir = Paths.get(BASE_UPLOAD_DIR, String.valueOf(candidateId));
            Files.createDirectories(candidateDir);
            Path filePath = candidateDir.resolve(fileName);
            Files.write(filePath, file.getBytes());
            candidate.setResumeUrl(filePath.toString());
            candidateRepository.save(candidate);
            return new ResumeUploadResponse("Resume uploaded successfully", filePath.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }
    }
}