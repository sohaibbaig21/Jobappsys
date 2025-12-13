package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID resumeId;

    @ManyToOne // Changed to ManyToOne relationship with Applicant
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant; // Link to the Applicant entity

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_size_kb")
    private Integer fileSizeKb;

    @Column(name = "is_primary")
    @Builder.Default
    private boolean isPrimary = false;

    @Column(name = "uploaded_at")
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // Removed ManyToOne relationship to Applicant as applicant_id is now a direct UUID column
}
