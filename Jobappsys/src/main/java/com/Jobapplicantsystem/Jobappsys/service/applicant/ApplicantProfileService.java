package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicantProfileDto;
import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.model.Resume; // Import Resume model
import com.Jobapplicantsystem.Jobappsys.model.ApplicantEducation; // Import ApplicantEducation model
import com.Jobapplicantsystem.Jobappsys.model.ApplicantSkill; // Import ApplicantSkill model
import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicantEducationDto; // Corrected import for ApplicantEducationDto
import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicantSkillDto; // Import ApplicantSkillDto
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantEducationRepository; // Import ApplicantEducationRepository
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantSkillRepository; // Import ApplicantSkillRepository
import com.Jobapplicantsystem.Jobappsys.repository.ResumeRepository; // Import ResumeRepository
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantProfileService {

    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
    private final ResumeRepository resumeRepository; // Inject ResumeRepository
    private final ApplicantEducationRepository applicantEducationRepository; // Inject ApplicantEducationRepository
    private final ApplicantSkillRepository applicantSkillRepository; // Inject ApplicantSkillRepository

    // 1. Update Profile (Name + Details)
    public void updateProfile(String email, ApplicantProfileDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Applicant applicant = getOrCreateApplicant(user);

        // Update User Name
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        // Update Applicant Details
        if (request.getPhone() != null) applicant.setPhone(request.getPhone());
        if (request.getCity() != null) applicant.setCity(request.getCity());
        if (request.getPostalCode() != null) applicant.setPostalCode(request.getPostalCode());

        userRepository.save(user);
        applicantRepository.save(applicant);
    }

    // 2. Get Profile Data
    public ApplicantProfileDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Applicant applicant = getOrCreateApplicant(user);

        ApplicantProfileDto response = new ApplicantProfileDto();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(applicant.getPhone());
        response.setCity(applicant.getCity());
        response.setPostalCode(applicant.getPostalCode());
        response.setEducation(applicant.getEducation().stream()
                .map(this::mapEducationToDto)
                .collect(Collectors.toList()));
        response.setSkills(applicant.getSkills().stream()
                .map(this::mapSkillToDto)
                .collect(Collectors.toList()));

        return response;
    }

    // 3. Update Resume
    public void updateResume(String email, String fileUrl, String fileName, Integer fileSizeKb) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Applicant applicant = getOrCreateApplicant(user);

        // Create and save new Resume entity
        Resume newResume = Resume.builder()
                .applicant(applicant)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSizeKb(fileSizeKb)
                .isPrimary(true)
                .uploadedAt(LocalDateTime.now())
                .build();
        resumeRepository.save(newResume);

        // Update applicant's current resume reference
        applicant.setResumeFilename(fileUrl);
        applicantRepository.save(applicant);
    }

    // New method to get applicant ID by email
    public Long getApplicantIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Applicant applicant = getOrCreateApplicant(user);
        return applicant.getId();
    }

    // Education CRUD Operations
    public ApplicantEducationDto addEducation(String email, ApplicantEducationDto educationDto) {
        Applicant applicant = getApplicantByEmail(email);
        ApplicantEducation education = mapDtoToEducation(educationDto);
        education.setApplicant(applicant);
        ApplicantEducation savedEducation = applicantEducationRepository.save(education);
        return mapEducationToDto(savedEducation);
    }

    public ApplicantEducationDto updateEducation(String email, UUID educationId, ApplicantEducationDto educationDto) {
        Applicant applicant = getApplicantByEmail(email);
        ApplicantEducation existingEducation = applicantEducationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education entry not found"));

        if (!existingEducation.getApplicant().getId().equals(applicant.getId())) {
            throw new RuntimeException("Unauthorized to update this education entry");
        }

        // Update fields
        existingEducation.setDegree(educationDto.getDegree());
        existingEducation.setMajor(educationDto.getMajor());
        existingEducation.setUniversity(educationDto.getUniversity());
        existingEducation.setStartDate(educationDto.getStartDate());
        existingEducation.setEndDate(educationDto.getEndDate());

        ApplicantEducation updatedEducation = applicantEducationRepository.save(existingEducation);
        return mapEducationToDto(updatedEducation);
    }

    public void deleteEducation(String email, UUID educationId) {
        Applicant applicant = getApplicantByEmail(email);
        ApplicantEducation existingEducation = applicantEducationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education entry not found"));

        if (!existingEducation.getApplicant().getId().equals(applicant.getId())) {
            throw new RuntimeException("Unauthorized to delete this education entry");
        }
        applicantEducationRepository.delete(existingEducation);
    }

    public List<ApplicantEducationDto> getApplicantEducation(String email) {
        Applicant applicant = getApplicantByEmail(email);
        return applicant.getEducation().stream()
                .map(this::mapEducationToDto)
                .collect(Collectors.toList());
    }

    // Skill CRUD Operations
    public ApplicantSkillDto addSkill(String email, ApplicantSkillDto skillDto) {
        Applicant applicant = getApplicantByEmail(email);
        ApplicantSkill skill = mapDtoToSkill(skillDto);
        skill.setApplicant(applicant);
        ApplicantSkill savedSkill = applicantSkillRepository.save(skill);
        return mapSkillToDto(savedSkill);
    }

    public ApplicantSkillDto updateSkill(String email, UUID skillId, ApplicantSkillDto skillDto) {
        Applicant applicant = getApplicantByEmail(email);
        ApplicantSkill existingSkill = applicantSkillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill entry not found"));

        if (!existingSkill.getApplicant().getId().equals(applicant.getId())) {
            throw new RuntimeException("Unauthorized to update this skill entry");
        }

        // Update fields
        existingSkill.setSkillName(skillDto.getSkillName());
        existingSkill.setProficiencyLevel(skillDto.getProficiencyLevel());
        existingSkill.setYearsOfExperience(skillDto.getYearsOfExperience());

        ApplicantSkill updatedSkill = applicantSkillRepository.save(existingSkill);
        return mapSkillToDto(updatedSkill);
    }

    public void deleteSkill(String email, UUID skillId) {
        Applicant applicant = getApplicantByEmail(email);
        ApplicantSkill existingSkill = applicantSkillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill entry not found"));

        if (!existingSkill.getApplicant().getId().equals(applicant.getId())) {
            throw new RuntimeException("Unauthorized to delete this skill entry");
        }
        applicantSkillRepository.delete(existingSkill);
    }

    public List<ApplicantSkillDto> getApplicantSkills(String email) {
        Applicant applicant = getApplicantByEmail(email);
        return applicant.getSkills().stream()
                .map(this::mapSkillToDto)
                .collect(Collectors.toList());
    }

    // Helper method to get Applicant by email
    private Applicant getApplicantByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getOrCreateApplicant(user);
    }

    // Helper method: Maps DTO to Entity
    private ApplicantEducation mapDtoToEducation(ApplicantEducationDto dto) {
        return ApplicantEducation.builder()
                .id(dto.getId() != null ? dto.getId() : UUID.randomUUID())
                .degree(dto.getDegree())
                .major(dto.getMajor())
                .university(dto.getUniversity())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    // Helper method: Maps Entity to DTO
    private ApplicantEducationDto mapEducationToDto(ApplicantEducation education) {
        return ApplicantEducationDto.builder()
                .id(education.getId())
                .degree(education.getDegree())
                .major(education.getMajor())
                .university(education.getUniversity())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .build();
    }

    // Helper method: Maps DTO to Entity (Skill)
    private ApplicantSkill mapDtoToSkill(ApplicantSkillDto dto) {
        return ApplicantSkill.builder()
                .applicantSkillId(dto.getApplicantSkillId() != null ? dto.getApplicantSkillId() : UUID.randomUUID())
                .skillName(dto.getSkillName())
                .proficiencyLevel(dto.getProficiencyLevel())
                .yearsOfExperience(dto.getYearsOfExperience())
                .build();
    }

    // Helper method: Maps Entity to DTO (Skill)
    private ApplicantSkillDto mapSkillToDto(ApplicantSkill skill) {
        return ApplicantSkillDto.builder()
                .applicantSkillId(skill.getApplicantSkillId())
                .skillName(skill.getSkillName())
                .proficiencyLevel(skill.getProficiencyLevel())
                .yearsOfExperience(skill.getYearsOfExperience())
                .build();
    }

    // Helper method
    private Applicant getOrCreateApplicant(User user) {
        Applicant applicant = user.getApplicant();
        if (applicant == null) {
            return applicantRepository.findByUserId(user.getId())
                    .orElseGet(() -> {
                        Applicant newApp = Applicant.builder().user(user).build();
                        return applicantRepository.save(newApp);
                    });
        }
        return applicant;
    }
}