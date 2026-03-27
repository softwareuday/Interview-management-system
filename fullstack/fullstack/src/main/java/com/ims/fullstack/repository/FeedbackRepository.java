
package com.ims.fullstack.repository;

import com.ims.fullstack.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByInterviewRound_Id(Long roundId);
    List<Feedback> findBySubmittedBy_Id(Long recruiterId);
    List<Feedback> findByInterviewRound_Candidate_Id(Long candidateId);
}