package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.service.employer.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs") // <--- This matches the URL you are trying to reach
public class JobSearchController {

    @Autowired
    private JobPostService jobPostService;

    // GET http://localhost:8081/api/jobs
    @GetMapping
    public ResponseEntity<List<JobPostResponse>> getAllJobs() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }

    // GET http://localhost:8081/api/jobs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<JobPostResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostService.getJobPostById(id));
    }
}