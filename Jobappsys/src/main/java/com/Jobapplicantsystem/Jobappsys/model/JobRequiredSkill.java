package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "job_required_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequiredSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID jobSkillId;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "importance_level")
    private String importanceLevel; // REQUIRED, PREFERRED, NICE_TO_HAVE
}
