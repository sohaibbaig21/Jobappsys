package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL)
    private List<ApplicationAnswer> answers;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL)
    private List<ApplicationStatusHistory> statusHistory;
}
