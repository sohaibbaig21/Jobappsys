package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reportId;

    @Column(name = "report_reason", nullable = false)
    private String reportReason;

    @Column(name = "report_description")
    private String description;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @Column(name = "status")
    private String status; // PENDING, UNDER_REVIEW, RESOLVED, DISMISSED

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne
    @JoinColumn(name = "reported_by_user_id", nullable = false)
    private User reporter;
}
