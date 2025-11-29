package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.service.applicant.ApplicantService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/search")
@RequiredArgsConstructor
public class JobSearchController {

    private final ApplicantService applicantService;

    @GetMapping
    public ResponseEntity<List<JobPost>> getAllJobs() {
        return ResponseEntity.ok(applicantService.getAllJobs());
    }
}
