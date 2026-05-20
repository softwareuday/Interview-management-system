package com.ims.fullstack.repository;

import com.ims.fullstack.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByRecruiter_Id(Long recruiterId);
}