package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicationRequest;
import com.Jobapplicantsystem.Jobappsys.model.JobApplication;
import com.Jobapplicantsystem.Jobappsys.service.applicant.ApplicationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applicant/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/apply")
    public ResponseEntity<JobApplication> apply(@RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.apply(getEmail(), request));
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> myApplications() {
        return ResponseEntity.ok(applicationService.myApplications(getEmail()));
    }
}
