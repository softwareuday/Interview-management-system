package com.ims.fullstack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    private String website;

    @Column(length = 500)
    private String address;

    private String companyType; // e.g., "Private", "Public", "Startup"

    @Column(unique = true)
    private String cin; // Company Identification Number

    @Column(unique = true)
    private String gstin;

    private String incorporationCertificateUrl; // file path

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;
}