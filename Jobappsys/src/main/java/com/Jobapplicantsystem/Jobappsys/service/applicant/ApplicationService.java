package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicationAnswerRequest;
import com.Jobapplicantsystem.Jobappsys.dto.request.JobApplicationRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobApplicationResponse;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.exception.ResourceNotFoundException;
import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.model.JobApplication;
import com.Jobapplicantsystem.Jobappsys.model.JobApplicationAnswer;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.model.JobPostQuestion;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobApplicationAnswerRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobApplicationRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostQuestionRepository;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import com.Jobapplicantsystem.Jobappsys.util.SupabaseStorageClient;
import com.Jobapplicantsystem.Jobappsys.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostRepository jobPostRepository;
    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final SupabaseStorageClient supabaseStorageClient;
    private final JobPostQuestionRepository jobPostQuestionRepository;
    private final JobApplicationAnswerRepository jobApplicationAnswerRepository;

    // Removed ObjectMapper as it's no longer directly used for JSON string conversions

    @Transactional
public JobApplicationResponse applyForJob(Long jobPostId, Long applicantId, JobApplicationRequest applicationRequest, MultipartFile resumeFile) {
    System.out.println("=============================================");
    System.out.println("🔵 SERVICE: applyForJob called");
    System.out.println("Job Post ID: " + jobPostId);
    System.out.println("Applicant ID: " + applicantId);
    System.out.println("Request: " + applicationRequest);
    System.out.println("Answers count: " + (applicationRequest != null && applicationRequest.getAnswers() != null ? applicationRequest.getAnswers().size() : "NULL"));
    System.out.println("Resume File: " + (resumeFile != null ? resumeFile.getOriginalFilename() : "NULL"));
    System.out.println("=============================================");
    
    try {
        logger.info("Applying for job post ID: {} by applicant ID: {}", jobPostId, applicantId);

        System.out.println("🔵 SERVICE STEP 1: Finding job post...");
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> {
                    System.out.println("❌ Job post not found with ID: " + jobPostId);
                    logger.error("Job post not found with ID: {}", jobPostId);
                    return new ResourceNotFoundException("Job post not found with ID: " + jobPostId);
                });
        System.out.println("✅ Job post found: " + jobPost.getTitle());

        System.out.println("🔵 SERVICE STEP 2: Finding applicant...");
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> {
                    System.out.println("❌ Applicant not found with ID: " + applicantId);
                    logger.error("Applicant not found with ID: {}", applicantId);
                    return new ResourceNotFoundException("Applicant not found with ID: " + applicantId);
                });
        System.out.println("✅ Applicant found: " + applicant.getUser().getFirstName());

        System.out.println("🔵 SERVICE STEP 3: Creating job application object...");
        JobApplication jobApplication = new JobApplication();
        jobApplication.setJobPost(jobPost);
        jobApplication.setApplicant(applicant);
        jobApplication.setStatus("APPLIED");
        jobApplication.setAppliedAt(LocalDateTime.now());
        System.out.println("✅ Job application object created");

        // Handle resume file upload (mandatory: either uploaded now or already present on applicant profile)
        System.out.println("🔵 SERVICE STEP 4: Handling resume (mandatory)...");
        String resumeFilenameToUse = null;
        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                System.out.println("Uploading resume file: " + resumeFile.getOriginalFilename());
                String uploaded = supabaseStorageClient.uploadFile(resumeFile, applicant.getUser().getId().toString());
                resumeFilenameToUse = uploaded;
                // Persist on Applicant profile for future applies
                applicant.setResumeFilename(uploaded);
                applicantRepository.save(applicant);
                System.out.println("✅ Resume uploaded: " + uploaded);
            } catch (Exception e) {
                System.out.println("❌ Resume upload failed: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to upload resume", e);
            }
        } else if (applicant.getResumeFilename() != null && !applicant.getResumeFilename().isBlank()) {
            resumeFilenameToUse = applicant.getResumeFilename();
            System.out.println("ℹ️ Using existing profile resume: " + resumeFilenameToUse);
        } else {
            System.out.println("❌ No resume provided and no existing resume on profile");
            throw new IllegalArgumentException("Resume is required to apply for this job");
        }

        // set on application
        jobApplication.setResumeFilename(resumeFilenameToUse);

        System.out.println("🔵 SERVICE STEP 5: Processing answers...");

            // Convert answers to JobApplicationAnswer entities
            List<JobApplicationAnswer> answers = applicationRequest.getAnswers().stream()
                    .map(answerRequest -> {
                        logger.debug("Processing answer for question ID: {}", answerRequest.getQuestionId());
                        // Fetch the actual JobPostQuestion entity
                        JobPostQuestion jobPostQuestion = jobPostQuestionRepository.findById(answerRequest.getQuestionId())
                                .orElseThrow(() -> {
                                    logger.error("Job question not found with ID: {}", answerRequest.getQuestionId());
                                    return new ResourceNotFoundException("Job question not found with ID: " + answerRequest.getQuestionId());
                                });
                        logger.debug("Found job question: {}", jobPostQuestion.getQuestionText());

                        JobApplicationAnswer newAnswer = JobApplicationAnswer.builder()
                                .jobQuestion(jobPostQuestion)
                                .answerText(answerRequest.getAnswerText())
                                .jobApplication(jobApplication)
                                .build();
                        logger.debug("Created new JobApplicationAnswer for question ID: {}", jobPostQuestion.getId());
                        // Explicitly save the answer before adding to the list
                        // This might help in cases where cascade is not behaving as expected or for debugging
                        jobApplicationAnswerRepository.save(newAnswer);
                        logger.debug("Saved JobApplicationAnswer with ID: {}", newAnswer.getId());
                        return newAnswer;
                    })
                    .collect(Collectors.toList());
            jobApplication.setAnswers(answers);
            logger.debug("Collected {} answers for the application.", answers.size());

            JobApplication savedApplication = jobApplicationRepository.save(jobApplication);
            logger.info("Job application saved successfully with ID: {}", savedApplication.getId());

            // Validate required questions (assuming jobPost.getQuestions() returns List<JobPostQuestion>)
            List<JobPostQuestion> jobPostQuestions = jobPost.getQuestions();
            if (jobPostQuestions != null) {
                logger.debug("Validating {} required questions.", jobPostQuestions.size());
                for (JobPostQuestion question : jobPostQuestions) {
                    if (question.getIsRequired()) { // Use getIsRequired() instead of isRequired()
                        boolean answered = savedApplication.getAnswers().stream()
                                .anyMatch(answer -> answer.getJobQuestion().getId().equals(question.getId())); // Use getId() for JobPostQuestion
                        if (!answered) {
                            logger.error("Required question not answered: {}", question.getQuestionText());
                            throw new IllegalArgumentException("Required question not answered: " + question.getQuestionText());
                        }
                    }
                }
                logger.debug("All required questions answered.");
            } else {
                logger.debug("No questions to validate for this job post.");
            }

            return mapToJobApplicationResponse(savedApplication);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found during job application: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument during job application: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred during job application: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected server error", e);
        }
    }

    public JobApplicationResponse updateApplicationStatus(Long applicationId, String status) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + applicationId));

        jobApplication.setStatus(status);
        JobApplication updatedApplication = jobApplicationRepository.save(jobApplication);
        return mapToJobApplicationResponse(updatedApplication);
    }

    public List<JobApplication> myApplications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        Applicant applicant = applicantRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found for user with email: " + email));

        return jobApplicationRepository.findByApplicantId(applicant.getId());
    }

    public List<JobApplication> getApplicationsForJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found with ID: " + jobPostId));
        return jobApplicationRepository.findByJobPostId(jobPost.getId());
    }

    @Transactional
    public void deleteApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + applicationId));
        // Optionally delete associated resume file
        if (jobApplication.getResumeFilename() != null) {
            // The subDirectory will be "resumes" as defined in storeFile
            // This part of the code was not updated in the edit hint, so it remains as is.
            // If the intent was to remove this, a separate edit would be needed.
            // For now, keeping it as is to avoid introducing unrelated changes.
            // fileStorageService.deleteFile(jobApplication.getResumeFilename(), "resumes"); // This line was removed from the new_code
        }
        jobApplicationRepository.delete(jobApplication);
    }

    public JobApplicationResponse mapToJobApplicationResponse(JobApplication jobApplication) {
        logger.debug("Mapping JobApplication to JobApplicationResponse for application ID: {}", jobApplication.getId());
        JobPost jobPost = jobApplication.getJobPost();
        logger.debug("Retrieved JobPost for mapping: {}", jobPost.getTitle());
        Applicant applicant = jobApplication.getApplicant();
        logger.debug("Retrieved Applicant for mapping: {} {}", applicant.getUser().getFirstName(), applicant.getUser().getLastName());

        return JobApplicationResponse.builder()
                .id(jobApplication.getId())
                .jobPost(JobPostResponse.builder()
                        .id(jobPost.getId())
                        .title(jobPost.getTitle())
                        .description(jobPost.getDescription())
                        .location(jobPost.getLocation())
                        .employmentType(jobPost.getEmploymentType())
                        .salary(jobPost.getSalary())
                        .build())
                .applicantId((Long) applicant.getId())
                .applicantName(applicant.getUser().getFirstName() + " " + applicant.getUser().getLastName()) // Construct full name
                .status(jobApplication.getStatus())
                .appliedAt(DateUtil.formatDateTime(jobApplication.getAppliedAt()))
                .resumeFilename(jobApplication.getResumeFilename())
                .answers(jobApplication.getAnswers().stream()
                        .map(answer -> new ApplicationAnswerRequest(answer.getJobQuestion().getId(), answer.getAnswerText())) // Use getId() for JobPostQuestion
                        .collect(Collectors.toList()))
                .build();
    }
}