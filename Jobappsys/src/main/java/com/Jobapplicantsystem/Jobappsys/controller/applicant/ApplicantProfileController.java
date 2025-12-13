package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicantProfileDto; // <--- DTO IMPORT
import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicantEducationDto; // Import ApplicantEducationDto
import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicantSkillDto; // Import ApplicantSkillDto
import com.Jobapplicantsystem.Jobappsys.service.applicant.ApplicantProfileService; // <--- SERVICE IMPORT
import com.Jobapplicantsystem.Jobappsys.util.SupabaseStorageClient; // Import SupabaseStorageClient
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List; // Import List
import java.util.Map;
import java.util.UUID; // Import UUID

@RestController
@RequestMapping("/api/applicant/profile")
@RequiredArgsConstructor
public class ApplicantProfileController {

    private final ApplicantProfileService applicantProfileService;
    private final SupabaseStorageClient supabaseStorageClient; // Inject SupabaseStorageClient

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // 1. Get Profile
    @GetMapping
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok(applicantProfileService.getProfile(getEmail()));
    }

    // 2. Update Profile Info
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody ApplicantProfileDto request) {
        applicantProfileService.updateProfile(getEmail(), request);
        return ResponseEntity.ok(Map.of("message", "Profile Updated Successfully"));
    }

    // 3. Upload Resume
    @PostMapping(path = "/upload-resume", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }
        try {
            String userId = applicantProfileService.getApplicantIdByEmail(getEmail()).toString();
            String fileUrl = supabaseStorageClient.uploadFile(file, userId);
            String originalFileName = file.getOriginalFilename(); // Extract original file name
            Integer fileSizeKb = (int) (file.getSize() / 1024); // Calculate file size in KB
            applicantProfileService.updateResume(getEmail(), fileUrl, originalFileName, fileSizeKb);
            return ResponseEntity.ok(Map.of("fileUrl", fileUrl, "message", "Resume uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading resume: " + e.getMessage());
        }
    }

    // 4. Get Applicant Education
    @GetMapping("/education")
    public ResponseEntity<List<ApplicantEducationDto>> getApplicantEducation() {
        return ResponseEntity.ok(applicantProfileService.getApplicantEducation(getEmail()));
    }

    // 5. Add Applicant Education
    @PostMapping("/education")
    public ResponseEntity<ApplicantEducationDto> addEducation(@RequestBody ApplicantEducationDto educationDto) {
        ApplicantEducationDto savedEducation = applicantProfileService.addEducation(getEmail(), educationDto);
        return ResponseEntity.ok(savedEducation);
    }

    // 6. Update Applicant Education
    @PutMapping("/education/{id}")
    public ResponseEntity<ApplicantEducationDto> updateEducation(@PathVariable UUID id, @RequestBody ApplicantEducationDto educationDto) {
        ApplicantEducationDto updatedEducation = applicantProfileService.updateEducation(getEmail(), id, educationDto);
        return ResponseEntity.ok(updatedEducation);
    }

    // 7. Delete Applicant Education
    @DeleteMapping("/education/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable UUID id) {
        applicantProfileService.deleteEducation(getEmail(), id);
        return ResponseEntity.noContent().build();
    }

    // 8. Get Applicant Skills
    @GetMapping("/skills")
    public ResponseEntity<List<ApplicantSkillDto>> getApplicantSkills() {
        return ResponseEntity.ok(applicantProfileService.getApplicantSkills(getEmail()));
    }

    // 9. Add Applicant Skill
    @PostMapping("/skills")
    public ResponseEntity<ApplicantSkillDto> addSkill(@RequestBody ApplicantSkillDto skillDto) {
        ApplicantSkillDto savedSkill = applicantProfileService.addSkill(getEmail(), skillDto);
        return ResponseEntity.ok(savedSkill);
    }

    // 10. Update Applicant Skill
    @PutMapping("/skills/{id}")
    public ResponseEntity<ApplicantSkillDto> updateSkill(@PathVariable UUID id, @RequestBody ApplicantSkillDto skillDto) {
        ApplicantSkillDto updatedSkill = applicantProfileService.updateSkill(getEmail(), id, skillDto);
        return ResponseEntity.ok(updatedSkill);
    }

    // 11. Delete Applicant Skill
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable UUID id) {
        applicantProfileService.deleteSkill(getEmail(), id);
        return ResponseEntity.noContent().build();
    }
}