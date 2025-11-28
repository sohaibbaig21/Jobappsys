package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // For login
    Optional<User> findByEmail(String email);

    // To check if email is already registered
    boolean existsByEmail(String email);
}
