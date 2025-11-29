package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.response.ApplicationResponse;

import java.util.List;
public interface ApplicationReviewService {
    
    void acceptApplication(Long applicationId);
    void rejectApplication(Long applicationId);
    List<ApplicationResponse> getApplicationsByJobPost(Long jobPostId);
}
