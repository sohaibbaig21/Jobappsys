package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "job_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    @Column(name = "employment_type")
    private String employmentType;

    private Double salary;

    // Replaced questionsJson with OneToMany relationship
    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore // Prevents infinite recursion during JSON serialization
    private List<JobPostQuestion> questions;

    // FIX: Link directly to User.
    // This solves the "cannot find symbol Employer" error.
    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = true)
    private User employer;

}