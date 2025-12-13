package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_status_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String status; // e.g., PENDING, ACCEPTED, REJECTED

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private JobApplication jobApplication;
}
