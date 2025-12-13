package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.ApplicantSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicantSkillRepository extends JpaRepository<ApplicantSkill, UUID> {
    List<ApplicantSkill> findByApplicantId(Long applicantId);
}
