
package com.ims.fullstack.repository;

import com.ims.fullstack.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiter_Id(Long recruiterId);

    // Changed: status is now String
    Page<Job> findByStatus(String status, Pageable pageable);

    // Changed: status is now String
    long countByRecruiter_IdAndStatus(Long recruiterId, String status);

}
