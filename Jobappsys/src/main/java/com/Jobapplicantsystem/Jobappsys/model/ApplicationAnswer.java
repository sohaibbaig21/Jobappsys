package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID answerId;

    @Column(name = "answer_text")
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private JobApplication jobApplication;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private JobPostQuestion jobPostQuestion; // Changed from JobQuestion to JobPostQuestion

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
