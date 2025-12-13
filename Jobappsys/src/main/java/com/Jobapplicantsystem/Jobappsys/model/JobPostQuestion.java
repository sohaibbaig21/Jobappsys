package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_post_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_type")
    private String questionType; // SHORT_TEXT, LONG_TEXT, YES_NO, MULTIPLE_CHOICE, NUMBER

    @Column(name = "is_required")
    @Builder.Default
    private Boolean isRequired = true;

    @Column(name = "question_order")
    private Integer questionOrder;

    @Column(name = "options")
    private String options; // Stores comma-separated options for MULTIPLE_CHOICE or SINGLE_CHOICE

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
