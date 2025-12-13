package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJobPostId(Long jobPostId);

    List<JobApplication> findByApplicantId(Long applicantId);

    // Prevent duplicate applications by same applicant to the same job
    boolean existsByJobPostIdAndApplicantId(Long jobPostId, Long applicantId);
}