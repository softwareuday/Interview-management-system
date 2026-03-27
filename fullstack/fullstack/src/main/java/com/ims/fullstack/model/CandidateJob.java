
package com.ims.fullstack.model;

import com.ims.fullstack.model.enums.CandidateStage;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
    @Enumerated(EnumType.STRING)
    private CandidateStage stage = CandidateStage.APPLIED;
    @Column(name = "applied_at")
    private LocalDateTime appliedAt = LocalDateTime.now();
    @Column(name = "current_round")
    private String currentRound;
}