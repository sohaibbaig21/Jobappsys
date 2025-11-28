package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private String employmentType;
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private List<JobQuestion> questions;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private List<JobRequiredSkill> requiredSkills;
}
