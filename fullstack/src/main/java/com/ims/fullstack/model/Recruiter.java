package com.ims.fullstack.model;

import com.ims.fullstack.model.enums.UserRole;
import com.ims.fullstack.model.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruiters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;
    private String companyName;

    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.RECRUITER;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // New fields for verification
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;

    private String otpCode;
    private LocalDateTime otpExpiry;

    @Builder.Default
    private boolean isEmailVerified = false;

    @Builder.Default
    private int registrationStep = 1;

    @OneToOne(mappedBy = "recruiter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Company company;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}