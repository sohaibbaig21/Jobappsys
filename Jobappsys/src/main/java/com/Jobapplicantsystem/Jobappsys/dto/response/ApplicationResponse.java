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
