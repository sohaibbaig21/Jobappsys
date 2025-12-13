package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    @Query("SELECT j FROM JobPost j LEFT JOIN FETCH j.questions LEFT JOIN FETCH j.employer WHERE j.id = :id")
    Optional<JobPost> findJobPostWithDetailsById(@Param("id") Long id);


    @Query("SELECT DISTINCT j FROM JobPost j LEFT JOIN FETCH j.employer LEFT JOIN FETCH j.questions WHERE " +
            // Avoid calling LOWER() on a null parameter to prevent provider errors
            "(:location IS NULL OR LOWER(j.location) LIKE :location) AND " +
            "(:type IS NULL OR j.employmentType = :type) AND " +
            "(:salary IS NULL OR (j.salary IS NOT NULL AND j.salary >= :salary))")
    List<JobPost> searchJobs(@Param("location") String location,
                             @Param("type") String type,
                             @Param("salary") Double salary);

    // --- EMPLOYER JOBS ---
    List<JobPost> findByEmployerId(Long employerId);
}