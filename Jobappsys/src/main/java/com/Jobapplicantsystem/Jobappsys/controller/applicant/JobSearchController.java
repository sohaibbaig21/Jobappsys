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