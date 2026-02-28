////package com.ims.fullstack.repository;
////
////import com.ims.fullstack.model.InterviewRound;
////import org.springframework.data.jpa.repository.JpaRepository;
////import java.util.List;
////
////public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Long> {
////
////    List<InterviewRound> findByJob_Id(Long jobId);
////
////    List<InterviewRound> findByCandidate_CandidateId(Long candidateId);
////
////    List<InterviewRound> findByRecruiter_Id(Long recruiterId);
////}
//package com.ims.fullstack.repository;
//
//import com.ims.fullstack.model.InterviewRound;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Long> {
//
//    // 🔹 By Job
//    List<InterviewRound> findByJob_Id(Long jobId);
//
//    // 🔹 By Candidate (✅ FIX FOR YOUR ERROR)
//    List<InterviewRound> findByCandidate_Id(Long candidateId);
//
//    // 🔹 By Recruiter
//    List<InterviewRound> findByRecruiter_Id(Long recruiterId);
//}
package com.ims.fullstack.repository;

import com.ims.fullstack.model.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Long> {
    List<InterviewRound> findByJob_Id(Long jobId);
    List<InterviewRound> findByCandidate_Id(Long candidateId);
    List<InterviewRound> findByRecruiter_Id(Long recruiterId);
}