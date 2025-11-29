package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import com.Jobapplicantsystem.Jobappsys.util.SupabaseStorageClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ApplicantProfileService {

    private final ApplicantRepository applicantRepository;
    private final SupabaseStorageClient supabaseStorageClient;

    // Fetch profile using logged-in user's email
    public Applicant getProfile(String email) {
        return applicantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Applicant profile not found"));
    }

    // Update basic applicant details
    public Applicant updateProfile(String email, Applicant updatedData) {
        Applicant applicant = applicantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        // Update only existing fields in Applicant entity
        applicant.setFullName(updatedData.getFullName());
        // We normally shouldn't change email from here; keep as is or update if provided
        if (updatedData.getEmail() != null && !updatedData.getEmail().isBlank()) {
            applicant.setEmail(updatedData.getEmail());
        }

        return applicantRepository.save(applicant);
    }

    // Upload resume to Supabase
    public String uploadResume(String email, MultipartFile file) throws IOException {
        // Upload resume to Supabase using email as identifier
        return supabaseStorageClient.uploadFile(file, email);
    }
}
