package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "applicant_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID applicantSkillId;

    @Column(name = "skill_name")
    private String skillName;

    @Column(name = "proficiency_level")
    private String proficiencyLevel;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    // Added to align with the more detailed schema
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
}
