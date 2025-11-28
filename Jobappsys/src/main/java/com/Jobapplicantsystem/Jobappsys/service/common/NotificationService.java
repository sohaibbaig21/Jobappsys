package com.Jobapplicantsystem.Jobappsys.service.common;

/**
 * NotificationService
 *
 * A simple contract for sending notifications within the system.
 * Currently left minimal – expand as needed (e.g., in-app, email, push).
 */
public interface NotificationService {
    void sendNotification(String to, String subject, String message);
}
