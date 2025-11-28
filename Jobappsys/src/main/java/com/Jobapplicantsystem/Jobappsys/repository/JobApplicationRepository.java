package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

}
