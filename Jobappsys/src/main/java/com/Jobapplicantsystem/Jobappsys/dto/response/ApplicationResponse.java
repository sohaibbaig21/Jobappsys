package com.Jobapplicantsystem.Jobappsys.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


// this information is split across 3-4 different tables (users, job_posts, applications, answers).
// This DTO merges them into one object so the Frontend doesn't have to make 4 different API calls.
public class ApplicationResponse {
    private Long id;
    private Long jobPostId;
    private String jobPostTitle;
    private Long applicantId;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private LocalDate appliedDate;
    private String status; // e.g., "APPLIED", "ACCEPTED", "REJECTED"
    private String resumeFilename; // Changed from resumeLink to resumeFilename
    private List<AnswerDto> answers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDto {
        private String question;
        private String answer;
    }
}
