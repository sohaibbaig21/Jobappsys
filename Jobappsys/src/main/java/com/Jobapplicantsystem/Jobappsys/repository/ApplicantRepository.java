package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

}
