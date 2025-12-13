package com.Jobapplicantsystem.Jobappsys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "applicants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link back to User (Login account)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore   // Prevents infinite loop on serialization
    private User user;

    private String phone;
    private String city;
    @Column(name = "postal_code")
    private String postalCode;

    // Replaced educationJson with OneToMany relationship
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    @JsonIgnore // Prevents infinite recursion during JSON serialization
    private List<ApplicantEducation> education;

    // Replaced experienceJson with OneToMany relationship
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    @JsonIgnore // Prevents infinite recursion during JSON serialization
    private List<ApplicantExperience> experience;

    // Replaced skillsJson with OneToMany relationship
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    @JsonIgnore // Prevents infinite recursion during JSON serialization
    private List<ApplicantSkill> skills;

    // Default resume filename for the profile
    @Column(name = "resume_filename")
    private String resumeFilename;
}
