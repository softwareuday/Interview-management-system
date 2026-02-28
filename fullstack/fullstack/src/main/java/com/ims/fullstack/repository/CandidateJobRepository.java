//package com.ims.fullstack.repository;
//
//import com.ims.fullstack.model.Candidate;
//import com.ims.fullstack.model.CandidateJob;
//import com.ims.fullstack.model.Job;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface CandidateJobRepository extends JpaRepository<CandidateJob, Long> {
//    Optional<CandidateJob> findByCandidateAndJob(Candidate candidate, Job job);
//}
package com.ims.fullstack.repository;

import com.ims.fullstack.model.Candidate;
import com.ims.fullstack.model.CandidateJob;
import com.ims.fullstack.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CandidateJobRepository extends JpaRepository<CandidateJob, Long> {
    Optional<CandidateJob> findByCandidateAndJob(Candidate candidate, Job job);
}