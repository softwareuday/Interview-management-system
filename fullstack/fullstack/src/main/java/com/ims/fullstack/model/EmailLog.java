

package com.ims.fullstack.model;

import com.ims.fullstack.model.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String subject;
    @Column(name = "body_preview", length = 1000)
    private String bodyPreview;
    @Enumerated(EnumType.STRING)
    private EmailStatus status = EmailStatus.SENT;
    @Column(name = "sent_at")
    private LocalDateTime sentAt = LocalDateTime.now();
    @Column(name = "provider_message_id")
    private String providerMessageId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private InterviewRound interviewRound;
}