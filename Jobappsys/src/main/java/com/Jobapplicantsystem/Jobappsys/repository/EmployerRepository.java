package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {

}
