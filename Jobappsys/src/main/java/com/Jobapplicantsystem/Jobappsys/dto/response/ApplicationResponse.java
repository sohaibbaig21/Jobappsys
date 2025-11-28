package com.Jobapplicantsystem.Jobappsys.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long applicantId;
    private Long jobPostId;
    private String status; // e.g., "PENDING", "ACCEPTED", "REJECTED"
    private String resumeLink;
    private String coverLetter;
}
