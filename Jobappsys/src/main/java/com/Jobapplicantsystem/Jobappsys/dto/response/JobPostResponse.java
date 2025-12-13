package com.Jobapplicantsystem.Jobappsys.dto.response;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
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
    private List<JobPostRequest.QuestionDto> questions;
    private List<String> options;
}
