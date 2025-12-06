package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    // --- THIS IS THE MISSING FIELD ---
    @Column(name = "is_active")
    @Builder.Default // Ensures it defaults to TRUE when using .builder()
    private Boolean isActive = true;

    // Enum for Roles
    public enum UserType {
        APPLICANT,
        EMPLOYER,
        ADMIN
    }
}