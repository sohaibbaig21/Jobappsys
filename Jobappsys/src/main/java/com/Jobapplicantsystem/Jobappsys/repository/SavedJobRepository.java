package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.model.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

}
