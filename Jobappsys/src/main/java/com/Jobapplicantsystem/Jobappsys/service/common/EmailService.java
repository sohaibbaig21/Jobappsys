package com.Jobapplicantsystem.Jobappsys.service.common;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
