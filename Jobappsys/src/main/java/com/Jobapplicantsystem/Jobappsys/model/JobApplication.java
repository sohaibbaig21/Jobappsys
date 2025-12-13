package com.Jobapplicantsystem.Jobappsys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    private String status; // APPLIED, HIRED, REJECTED

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    // Replaced answersJson with OneToMany relationship
    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore // Prevents infinite recursion during JSON serialization
    private List<JobApplicationAnswer> answers = new java.util.ArrayList<>();

    @Column(name = "resume_filename")
    private String resumeFilename;
}