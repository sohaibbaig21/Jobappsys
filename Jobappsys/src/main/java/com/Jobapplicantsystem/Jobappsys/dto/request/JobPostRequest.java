package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostRequest {
    private String title;
    private String description;
    private String location;
    private String employmentType; // e.g., Full-time, Part-time
    private Double salary;
}
