package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByUserEmail(String email);

    Optional<Applicant> findByUserId(Long userId);
}