package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private List<JobPost> jobPosts;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private List<EmployerReview> reviews;
}
