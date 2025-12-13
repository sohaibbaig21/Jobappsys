package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "employers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employer {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "employer_id", columnDefinition = "uuid")
    private UUID employerId;

    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_description", columnDefinition = "text")
    private String companyDescription;

    @Column(name = "company_website")
    private String companyWebsite;

    @Column(name = "company_size")
    private String companySize;

    @Column(name = "industry")
    private String industry;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
