package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

}
