package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.ApplicantEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicantEducationRepository extends JpaRepository<ApplicantEducation, UUID> {
    List<ApplicantEducation> findByApplicantId(Long applicantId);
}
