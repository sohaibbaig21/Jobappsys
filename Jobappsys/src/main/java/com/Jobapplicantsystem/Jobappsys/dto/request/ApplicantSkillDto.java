package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantSkillDto {
    private UUID applicantSkillId;
    private String skillName;
    private String proficiencyLevel;
    private Integer yearsOfExperience;
}
