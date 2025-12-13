package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.JobApplicationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobApplicationAnswerRepository extends JpaRepository<JobApplicationAnswer, Long> {
}
