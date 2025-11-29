package com.Jobapplicantsystem.Jobappsys.repository;

import com.Jobapplicantsystem.Jobappsys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // For login
    Optional<User> findByEmail(String email);

    // To check if email is already registered
    boolean existsByEmail(String email);
}
