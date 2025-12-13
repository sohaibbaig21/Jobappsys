package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.service.employer.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handles job search and retrieval operations for applicants:
//Search/Filter Jobs - Get a list of jobs with optional filters (location, employment type, sort)
//Get Job Details - Retrieve a single job post by its ID


@RestController
@RequestMapping("/api/jobs") // <--- This matches the URL you are trying to reach
public class JobSearchController {

    // Injects dependency (alternative to @RequiredArgsConstructor)

    // @RequiredArgsConstructor We use it because
    //Controller needs JobPostService to fetch job data
    //Spring automatically creates and injects the service instance


    @Autowired
    private JobPostService jobPostService;

    // GET http://localhost:8081/api/jobs?location=&type=&sort=
    @GetMapping
    public ResponseEntity<List<JobPostResponse>> getJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false, name = "type") String employmentType,
            @RequestParam(required = false, defaultValue = "relevant") String sort
    ) {
        return ResponseEntity.ok(jobPostService.searchAndFilter(location, employmentType, sort));
    }

    // GET http://localhost:8081/api/jobs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<JobPostResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostService.getJobPostById(id));
    }
}