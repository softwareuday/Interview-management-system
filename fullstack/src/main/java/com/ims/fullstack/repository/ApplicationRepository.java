

package com.ims.fullstack.repository;

import com.ims.fullstack.model.Application;
import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.model.Job;
import com.ims.fullstack.model.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByCandidateAndJob(Candidate candidate, Job job);
    Optional<Application> findByCandidate_IdAndJob_Id(Long candidateId, Long jobId);
    List<Application> findByCandidate_Id(Long candidateId);
    List<Application> findByJob_Id(Long jobId);
    long countByJob_Id(Long jobId);
    long countByJob_Recruiter_IdAndAppliedAtAfter(Long recruiterId, LocalDateTime after);
    long countByCandidate_IdAndStatusNotIn(Long candidateId, List<ApplicationStatus> statuses);
}