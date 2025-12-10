package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    // Custom Filter Query
    @Query("SELECT j FROM JobPost j WHERE " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:type IS NULL OR j.employmentType = :type) AND " +
            "(:salary IS NULL OR j.salary >= :salary)")
    List<JobPost> searchJobs(@Param("location") String location,
                             @Param("type") String type,
                             @Param("salary") Double salary);
}