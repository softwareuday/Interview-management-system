////package com.ims.fullstack.repository;
////
////import com.ims.fullstack.model.Candidate;
////import org.springframework.data.jpa.repository.JpaRepository;
////import java.util.Optional;
////import java.util.List;
////
////public interface CandidateRepository extends JpaRepository<Candidate, Long> {
////
////    Optional<Candidate> findByEmail(String email);
////
////    boolean existsByEmail(String email);
////
////    // 🆕 Added for Interview Scheduling
////    // Find all external candidates created by a specific recruiter
////    List<Candidate> findByIsExternalTrue();
////
////    // 🆕 Find all candidates created or updated by a recruiter (to show recruiter’s managed candidates)
////    List<Candidate> findByLastUpdatedBy(String recruiterEmail);
////}
//package com.ims.fullstack.repository;
//
//import com.ims.fullstack.model.Candidate;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface CandidateRepository extends JpaRepository<Candidate, Long> {
//
//    Optional<Candidate> findByEmail(String email);
//
//    boolean existsByEmail(String email);
//}


package com.ims.fullstack.repository;

import com.ims.fullstack.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String email);
    boolean existsByEmail(String email);
}