package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "applicant_educations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    private String degree;
    private String major;
    private String university;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
