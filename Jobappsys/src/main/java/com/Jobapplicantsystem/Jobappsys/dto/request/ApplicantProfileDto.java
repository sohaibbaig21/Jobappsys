package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ApplicantProfileDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String city;
    private String postalCode;
    private List<ApplicantEducationDto> education; // Add education list
    private List<ApplicantSkillDto> skills; // Add skills list
}