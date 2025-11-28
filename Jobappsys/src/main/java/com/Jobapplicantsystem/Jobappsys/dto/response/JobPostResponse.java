package com.Jobapplicantsystem.Jobappsys.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private Double salary;
    private Long employerId;
}
