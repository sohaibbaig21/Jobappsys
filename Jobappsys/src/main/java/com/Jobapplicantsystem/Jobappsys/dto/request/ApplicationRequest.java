package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    private Long applicantId;
    private Long jobPostId;
    private String resumeLink;
    private String coverLetter;
}
