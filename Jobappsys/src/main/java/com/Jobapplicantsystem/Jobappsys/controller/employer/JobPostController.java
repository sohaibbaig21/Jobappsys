package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.service.employer.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer/jobs")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;


    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobPostRequest request) {
        try {
            return ResponseEntity.ok(jobPostService.updateJobPost(id, request));
        } catch (NullPointerException e) {
            return ResponseEntity.status(400).body("Missing required question text."); // 400 Bad Request
        } catch (RuntimeException e) {
            if ("Job not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(e.getMessage()); // 404 Not Found
            }
            return ResponseEntity.status(500).body(e.getMessage()); // Other RuntimeExceptions as 500
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected server error occurred during job update."); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobPostService.deleteJobPost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostResponse> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostService.getJobPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> getAllJobs() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }
}