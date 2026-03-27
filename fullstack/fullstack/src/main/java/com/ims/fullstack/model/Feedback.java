
package com.ims.fullstack.model;

import com.ims.fullstack.model.enums.FeedbackResult;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer technicalRating;
    private Integer communicationRating;
    @Column(columnDefinition = "TEXT")
    private String comments;
    @Enumerated(EnumType.STRING)
    private FeedbackResult result = FeedbackResult.HOLD;
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private InterviewRound interviewRound;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private Recruiter submittedBy;
}