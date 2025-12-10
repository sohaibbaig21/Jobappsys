package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applicants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String phone;
    private String city;
    private String postalCode;
    private Boolean relocate;

    private String jobTitle;
    private String jobType;
    private String workAuth;

    @Column(columnDefinition = "TEXT")
    private String educationJson;

    @Column(columnDefinition = "TEXT")
    private String experienceJson;

    @Column(columnDefinition = "TEXT")
    private String skillsJson;

    @Column(columnDefinition = "TEXT")
    private String summary;

    // --- NEW FIELDS FOR DATA TRANSFER ---
    // @Transient means: "Don't save this in the applicants table,
    // just use it to carry data from the frontend."

    @Transient
    private String firstName;

    @Transient
    private String lastName;

    @Transient
    private String email;
}