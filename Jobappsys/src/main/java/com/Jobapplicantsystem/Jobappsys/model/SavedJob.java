package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savedJobId;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "saved_at")
    @Builder.Default
    private LocalDateTime savedAt = LocalDateTime.now();
}
