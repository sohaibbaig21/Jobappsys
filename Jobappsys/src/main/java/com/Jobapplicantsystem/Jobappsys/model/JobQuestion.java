package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;

    @OneToMany(mappedBy = "jobQuestion", cascade = CascadeType.ALL)
    private List<ApplicationAnswer> answers;
}
