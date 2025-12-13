package com.Jobapplicantsystem.Jobappsys.dto.response;

import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicationAnswerRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponse {
    private Long id;
    private JobPostResponse jobPost;
    private Long applicantId;
    private String applicantName;
    private String status;
    private String appliedAt;
    private String resumeFilename;
    private List<ApplicationAnswerRequest> answers;
}
