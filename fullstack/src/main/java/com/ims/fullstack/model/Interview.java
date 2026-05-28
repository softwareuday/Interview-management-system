
package com.ims.fullstack.model;

import jakarta.persistence.*;
import lombok.*;
import com.ims.fullstack.model.enums.InterviewMode;
import com.ims.fullstack.model.enums.InterviewStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long recruiterId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private LocalDate interviewDate;
    @Column(nullable = false)
    private LocalTime interviewTime;
    @Enumerated(EnumType.STRING)
    private InterviewMode mode;
    private String meetingLink;
    @Column(length = 1000)
    private String remarks;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status = InterviewStatus.SCHEDULED;
    private String resumeUrl;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}