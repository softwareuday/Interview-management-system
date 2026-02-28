//package com.ims.fullstack.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "candidates")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Candidate {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String fullName;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    private String resumeUrl;
//
//    private String phoneNumber;
//
//    private String location;
//
//    private String linkedinUrl;
//
//    @Column(length = 1000)
//    private String skills; // Comma-separated
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private UserRole role = UserRole.CANDIDATE;
//}
package com.ims.fullstack.model;

import com.ims.fullstack.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String resumeUrl;

    private String phoneNumber;

    private String location;

    private String linkedinUrl;

    @Column(length = 1000)
    private String skills;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.CANDIDATE;

    // ⭐ NEW: createdAt field with @PrePersist
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
