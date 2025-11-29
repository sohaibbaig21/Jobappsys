package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.response.ApplicationResponse;
import com.Jobapplicantsystem.Jobappsys.model.JobApplication; // Use your actual Entity name
import com.Jobapplicantsystem.Jobappsys.repository.JobApplicationRepository; // <--- The one you have!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // This annotation solves your "Parameter 0..." error
public class ApplicationReviewServiceImpl implements ApplicationReviewService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Override
    public void acceptApplication(Long applicationId) {
        // Example logic:
        // JobApplication app = jobApplicationRepository.findById(applicationId).orElseThrow();
        // app.setStatus("ACCEPTED");
        // jobApplicationRepository.save(app);
    }

    @Override
    public void rejectApplication(Long applicationId) {
        // Example logic:
        // JobApplication app = jobApplicationRepository.findById(applicationId).orElseThrow();
        // app.setStatus("REJECTED");
        // jobApplicationRepository.save(app);
    }

    @Override
    public List<ApplicationResponse> getApplicationsByJobPost(Long jobPostId) {
        // You will need to write the logic here to convert Entity -> DTO
        return null;
    }
}