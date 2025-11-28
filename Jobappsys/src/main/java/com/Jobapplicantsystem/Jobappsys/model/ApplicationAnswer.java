package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerText;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private JobApplication jobApplication;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private JobQuestion jobQuestion;
}
