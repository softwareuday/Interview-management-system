
package com.ims.fullstack.repository;

import com.ims.fullstack.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByEmail(String email);
    boolean existsByEmail(String email);
}