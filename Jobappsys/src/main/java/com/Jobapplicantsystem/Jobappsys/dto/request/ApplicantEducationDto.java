package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
// Service uses this dto to map objects in database
@Data
@Builder  // lets you create complex objects in a clean, readable way
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantEducationDto {
    private UUID id;
    private String degree;
    private String major;
    private String university;
    private LocalDate startDate;
    private LocalDate endDate;
}
