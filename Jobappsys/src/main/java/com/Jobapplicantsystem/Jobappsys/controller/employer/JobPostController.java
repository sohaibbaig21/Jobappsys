package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.service.employer.JobPostService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer/jobs")
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/create")
    public ResponseEntity<JobPostResponse> createJob(@RequestBody JobPostRequest request) {
        return ResponseEntity.ok(jobPostService.createJobPost(request));
    }

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> getMyJobs() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }
}
