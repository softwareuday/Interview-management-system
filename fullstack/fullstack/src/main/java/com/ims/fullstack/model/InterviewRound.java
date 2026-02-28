//package com.ims.fullstack.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.ims.fullstack.model.enums.RoundType;
//import com.ims.fullstack.model.enums.InterviewStatus;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "interview_rounds")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "candidate", "job", "recruiter"})
//public class InterviewRound {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String roundName;
//
//    @Enumerated(EnumType.STRING)
//    private RoundType roundType = RoundType.ONLINE;
//
//    @Column(length = 1000)
//    private String meetingLink;
//
//    private String joinToken;
//
//    private LocalDateTime interviewTime;
//
//    @Enumerated(EnumType.STRING)
//    private InterviewStatus status = InterviewStatus.SCHEDULED;
//
//    private Boolean feedbackSubmitted = false;
//
//    private LocalDateTime tokenExpiry;
//
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "candidate_id")
//    private Candidate candidate;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "job_id")
//    private Job job;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "recruiter_id")
//    private Recruiter recruiter;
//
//    @OneToOne(mappedBy = "interviewRound", cascade = CascadeType.ALL)
//    private Feedback feedback;
//
//    @OneToMany(mappedBy = "interviewRound", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<EmailLog> emailLogs;
//}

package com.ims.fullstack.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ims.fullstack.model.enums.RoundType;
import com.ims.fullstack.model.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interview_rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "candidate", "job", "recruiter"})
public class InterviewRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roundName;
    @Enumerated(EnumType.STRING)
    private RoundType roundType = RoundType.ONLINE;
    @Column(length = 1000)
    private String meetingLink;
    private String joinToken;
    private LocalDateTime interviewTime;
    @Enumerated(EnumType.STRING)
    private InterviewStatus status = InterviewStatus.SCHEDULED;
    private Boolean feedbackSubmitted = false;
    private LocalDateTime tokenExpiry;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;
    @OneToOne(mappedBy = "interviewRound", cascade = CascadeType.ALL)
    private Feedback feedback;
    @OneToMany(mappedBy = "interviewRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailLog> emailLogs;
}