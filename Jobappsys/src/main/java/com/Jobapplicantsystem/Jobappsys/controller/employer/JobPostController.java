package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.model.JobPost;
import com.Jobapplicantsystem.service.employer.JobPostService;

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
    public ResponseEntity<JobPost> createJob(@RequestBody JobPost job) {
        return ResponseEntity.ok(jobPostService.createJob(getEmail(), job));
    }

    @GetMapping
    public ResponseEntity<List<JobPost>> getMyJobs() {
        return ResponseEntity.ok(jobPostService.getEmployerJobs(getEmail()));
    }
}
