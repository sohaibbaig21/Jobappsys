package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<ApplicantSkill> skills;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<JobApplication> applications;
}
