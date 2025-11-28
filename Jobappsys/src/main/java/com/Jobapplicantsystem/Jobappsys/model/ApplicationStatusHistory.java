package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; // e.g., PENDING, ACCEPTED, REJECTED
    private LocalDateTime changedAt;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private JobApplication jobApplication;
}
