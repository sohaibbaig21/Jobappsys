package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;

    @Column(name = "user_id", nullable = false)
    private UUID userId; // Assuming this will be a UUID in User model

    @Column(name = "notification_type", nullable = false)
    private String notificationType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_read")
    @Builder.Default
    private boolean isRead = false;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // Removed ManyToOne relationship to User as user_id is now a direct UUID column
}
