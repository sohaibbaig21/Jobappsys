package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sharedWithEmail;
    private LocalDateTime sharedAt;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;
}
