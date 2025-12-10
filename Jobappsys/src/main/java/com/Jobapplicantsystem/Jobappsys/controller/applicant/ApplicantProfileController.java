package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applicant/profile")
@RequiredArgsConstructor
public class ApplicantProfileController {

    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> updateProfile(@RequestBody Applicant profileData) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 1. Fetch existing User (Account Info)
        User user = userRepository.findByEmail(email).orElseThrow();

        // 2. Fetch or Create Applicant (Profile Info)
        Applicant applicant = applicantRepository.findByEmail(email).orElse(new Applicant());

        // 3. Update USER fields (Name, Email) if provided
        if (profileData.getFirstName() != null && !profileData.getFirstName().isBlank()) {
            user.setFirstName(profileData.getFirstName());
        }
        if (profileData.getLastName() != null && !profileData.getLastName().isBlank()) {
            user.setLastName(profileData.getLastName());
        }
        // Be careful updating email as it changes their login!
        if (profileData.getEmail() != null && !profileData.getEmail().isBlank()) {
            user.setEmail(profileData.getEmail());
        }
        userRepository.save(user); // Save User table changes

        // 4. Update APPLICANT fields
        applicant.setUser(user);
        applicant.setPhone(profileData.getPhone());
        applicant.setCity(profileData.getCity());
        applicant.setPostalCode(profileData.getPostalCode());
        applicant.setRelocate(profileData.getRelocate());
        applicant.setJobTitle(profileData.getJobTitle());
        applicant.setJobType(profileData.getJobType());
        applicant.setWorkAuth(profileData.getWorkAuth());
        applicant.setSummary(profileData.getSummary());

        applicant.setEducationJson(profileData.getEducationJson());
        applicant.setExperienceJson(profileData.getExperienceJson());
        applicant.setSkillsJson(profileData.getSkillsJson());

        applicantRepository.save(applicant); // Save Applicant table changes

        return ResponseEntity.ok("Profile Updated Successfully");
    }

    @GetMapping
    public ResponseEntity<?> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicantRepository.findByEmail(email).orElse(new Applicant()));
    }
}