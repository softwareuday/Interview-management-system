

package com.ims.fullstack.model;

import jakarta.persistence.*;
import lombok.*;
import com.ims.fullstack.model.enums.ApplicationStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;
    private String resumeUrl;
    @Column(length = 5000)
    private String coverLetter;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
    private Integer atsScore;
    @Column(length = 1000)
    private String matchedKeywords;
    @Column(length = 1000)
    private String missingKeywords;
    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}