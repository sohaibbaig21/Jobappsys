package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.response.ApplicationResponse;
import com.Jobapplicantsystem.Jobappsys.model.JobApplication; // Use your actual Entity name
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.repository.JobApplicationRepository; // <--- The one you have!
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import com.Jobapplicantsystem.Jobappsys.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // This annotation solves your "Parameter 0..." error
@RequiredArgsConstructor
public class ApplicationReviewServiceImpl implements ApplicationReviewService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;

    @Override
    public void acceptApplication(Long applicationId) {
        if (applicationId == null) {
            throw new IllegalArgumentException("Application ID cannot be null.");
        }
        JobApplication app = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        app.setStatus("ACCEPTED");
        jobApplicationRepository.save(app);
    }

    @Override
    public void rejectApplication(Long applicationId) {
        if (applicationId == null) {
            throw new IllegalArgumentException("Application ID cannot be null.");
        }
        JobApplication app = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        app.setStatus("REJECTED");
        jobApplicationRepository.save(app);
    }

    @Override
    public List<ApplicationResponse> getApplicationsByJobPost(Long jobPostId, String employerEmail) {
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        JobPost jobPost = jobPostRepository.findJobPostWithDetailsById(jobPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found"));

        if (!jobPost.getEmployer().getId().equals(employer.getId())) {
            throw new ResourceNotFoundException("Job post not found or does not belong to this employer");
        }

        List<JobApplication> jobApplications = jobApplicationRepository.findByJobPostId(jobPostId);

        return jobApplications.stream().map(this::mapToApplicationResponse).collect(Collectors.toList());
    }

    private ApplicationResponse mapToApplicationResponse(JobApplication app) {
        // Safely get applicant details (assuming they are always present for a JobApplication)
        Applicant applicant = app.getApplicant();
        User applicantUser = applicant.getUser();

        return ApplicationResponse.builder()
                .id(app.getId())
                .jobPostId(app.getJobPost().getId())
                .jobPostTitle(app.getJobPost().getTitle())
                .applicantId(applicant.getId())
                .applicantName(applicantUser.getFirstName() + " " + applicantUser.getLastName())
                .applicantEmail(applicantUser.getEmail())
                .applicantPhone(applicant.getPhone())
                .appliedDate(app.getAppliedAt() != null ? app.getAppliedAt().toLocalDate() : null)
                .status(app.getStatus())
                .resumeFilename(app.getResumeFilename())
                .answers(app.getAnswers().stream()
                        .map(answer -> new ApplicationResponse.AnswerDto(
                                answer.getJobQuestion().getQuestionText(),
                                answer.getAnswerText()
                        ))
                        .collect(Collectors.toList()))
                .build();
    }
}