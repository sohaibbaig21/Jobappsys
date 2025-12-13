package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.model.JobPostQuestion;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostQuestionRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final JobPostQuestionRepository jobPostQuestionRepository; // Inject this

    // -------------------------------
    // 1. SEARCH & FILTER
    // -------------------------------
    public List<JobPostResponse> searchAndFilter(String location, String type, String sort) {

        String locationFilter = null;
        if (location != null && !location.isBlank()) {
            locationFilter = "%" + location.toLowerCase() + "%";
        }

        String typeFilter = (type == null || type.isBlank() || type.equals("All")) ? null : type;

        List<JobPost> jobs = jobPostRepository.searchJobs(locationFilter, typeFilter, null);

        if ("oldest".equals(sort)) {
            jobs.sort((a, b) -> Objects.requireNonNull(a.getId()).compareTo(Objects.requireNonNull(b.getId())));
        } else if ("newest".equals(sort)) {
            jobs.sort((a, b) -> Objects.requireNonNull(b.getId()).compareTo(Objects.requireNonNull(a.getId())));
        }

        return jobs.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // -------------------------------
    // 2. EMPLOYER-SPECIFIC JOBS
    // -------------------------------
    public List<JobPostResponse> getMyJobs(String email) {
        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return jobPostRepository.findByEmployerId(Objects.requireNonNull(employer.getId()))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // -------------------------------
    // 3. CREATE JOB POST
    // -------------------------------
    public JobPostResponse createJobPost(JobPostRequest request, String email) {
        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer account not found"));

        JobPost jobPost = mapToEntity(request);
        jobPost.setEmployer(employer);

        JobPost savedJob = jobPostRepository.save(jobPost);

        // Save JobPostQuestions if provided
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            List<JobPostQuestion> questions = request.getQuestions().stream()
                    .map(q -> JobPostQuestion.builder()
                            .jobPost(savedJob)
                            .questionText(Objects.requireNonNull(q.getQuestionText()))
                            .questionType(q.getQuestionType())
                            .isRequired(q.getIsRequired())
                            .questionOrder(q.getQuestionOrder())
                            .options(q.getOptions() != null ? String.join(",", q.getOptions()) : null) // Save options
                            .build())
                    .collect(Collectors.toList());
            jobPostQuestionRepository.saveAll(Objects.requireNonNull(questions));
        }

        return mapToResponse(savedJob);
    }

    // -------------------------------
    // 4. UPDATE JOB POST
    // -------------------------------
    public JobPostResponse updateJobPost(Long id, JobPostRequest request) {

        JobPost jobPost = jobPostRepository.findJobPostWithDetailsById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        jobPost.setLocation(request.getLocation());
        jobPost.setEmploymentType(request.getEmploymentType());
        jobPost.setSalary(request.getSalary());

        // Update questions (delete existing and save new ones)
        jobPostQuestionRepository.deleteByJobPost(jobPost);
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            List<JobPostQuestion> questions = request.getQuestions().stream()
                    .map(q -> JobPostQuestion.builder()
                            .jobPost(jobPost)
                            .questionText(Objects.requireNonNull(q.getQuestionText()))
                            .questionType(q.getQuestionType())
                            .isRequired(q.getIsRequired())
                            .questionOrder(q.getQuestionOrder())
                            .options(q.getOptions() != null ? String.join(",", q.getOptions()) : null) // Save options
                            .build())
                    .collect(Collectors.toList());
            jobPostQuestionRepository.saveAll(Objects.requireNonNull(questions));
        }

        JobPost updated = jobPostRepository.save(jobPost);
        return mapToResponse(updated);
    }

    // -------------------------------
    // 5. DELETE JOB POST
    // -------------------------------
    public void deleteJobPost(@NonNull Long id) {
        jobPostRepository.deleteById(id);
    }

    // -------------------------------
    // 6. GET BY ID
    // -------------------------------
    public JobPostResponse getJobPostById(Long id) {
        JobPost jobPost = jobPostRepository.findJobPostWithDetailsById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return mapToResponse(jobPost);
    }

    // -------------------------------
    // 7. GET ALL JOBS
    // -------------------------------
    public List<JobPostResponse> getAllJobPosts() {
        return jobPostRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // -------------------------------
    // 8. MAPPERS
    // -------------------------------
    private JobPostResponse mapToResponse(JobPost job) {
        JobPostResponse response = new JobPostResponse();

        response.setId(Objects.requireNonNull(job.getId()));
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setEmploymentType(job.getEmploymentType());
        response.setSalary(job.getSalary());

        // Handle potential null employer
        if (job.getEmployer() != null && job.getEmployer().getId() != null) {
            response.setEmployerId(job.getEmployer().getId());
        }

        // Map questions, handling potential null or empty list
        if (job.getQuestions() != null && !job.getQuestions().isEmpty()) {
            response.setQuestions(job.getQuestions().stream()
                    .map(q -> new JobPostRequest.QuestionDto(q.getQuestionText(), q.getQuestionType(), q.getIsRequired(), q.getQuestionOrder(),
                            (q.getOptions() != null && !q.getOptions().isEmpty()) ? List.of(q.getOptions().split(",")) : null)) // Map options
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private JobPost mapToEntity(JobPostRequest request) {

        return JobPost.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .salary(request.getSalary())
                .build();
    }
}
