package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_application_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication jobApplication;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private JobPostQuestion jobQuestion; // Changed from JobQuestion to JobPostQuestion

    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
