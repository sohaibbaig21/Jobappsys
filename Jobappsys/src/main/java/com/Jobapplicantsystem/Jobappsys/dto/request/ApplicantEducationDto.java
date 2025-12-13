package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
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
