package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.model.Applicant;
import com.Jobapplicantsystem.model.User;
import com.Jobapplicantsystem.repository.ApplicantRepository;
import com.Jobapplicantsystem.repository.UserRepository;
import com.Jobapplicantsystem.util.SupabaseStorageClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ApplicantProfileService {

    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final SupabaseStorageClient supabaseStorageClient;

    // Fetch profile using logged-in user's email
    public Applicant getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicantRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Applicant profile not found"));
    }

    // Update basic applicant details
    public Applicant updateProfile(String email, Applicant updatedData) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Applicant applicant = applicantRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        applicant.setAddress(updatedData.getAddress());
        applicant.setPhone(updatedData.getPhone());
        applicant.setExperience(updatedData.getExperience());
        applicant.setEducation(updatedData.getEducation());
        applicant.setSkills(updatedData.getSkills());

        return applicantRepository.save(applicant);
    }

    // Upload resume to Supabase
    public String uploadResume(String email, MultipartFile file) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String url = supabaseStorageClient.uploadFile(file, user.getId().toString());

        Applicant applicant = applicantRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        applicant.setResumeUrl(url);
        applicantRepository.save(applicant);

        return url;
    }
}
