package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "applicant_experiences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    private String title;
    private String company;
    private String location;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private String description;
}
