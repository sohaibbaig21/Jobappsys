package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.JobPostQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;

import java.util.UUID;

public interface JobPostQuestionRepository extends JpaRepository<JobPostQuestion, Long> {
    void deleteByJobPost(JobPost jobPost);
}
