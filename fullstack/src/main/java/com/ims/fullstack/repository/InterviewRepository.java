
package com.ims.fullstack.repository;

import com.ims.fullstack.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByRecruiterId(Long recruiterId);
    List<Interview> findByRecruiterIdAndInterviewDateAfter(Long recruiterId, LocalDate date);
    List<Interview> findByRecruiterIdAndInterviewDateBefore(Long recruiterId, LocalDate date);
    List<Interview> findByCandidate_Id(Long candidateId);
    long countByRecruiterIdAndInterviewDate(Long recruiterId, LocalDate date);
    long countByCandidate_IdAndInterviewDateGreaterThanEqual(Long candidateId, LocalDate date);
}