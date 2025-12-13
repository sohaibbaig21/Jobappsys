package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shared_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shareId;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne
    @JoinColumn(name = "shared_by_user_id", nullable = false)
    private User sharedByUser;

    @Column(name = "share_medium")
    private String shareMedium; // EMAIL, WHATSAPP, LINKEDIN, TWITTER, COPY_LINK

    @Column(name = "shared_at")
    @Builder.Default
    private LocalDateTime sharedAt = LocalDateTime.now();
}
