package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID skillId;

    @Column(name = "skill_name", nullable = false, unique = true)
    private String name;

    private String category;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
