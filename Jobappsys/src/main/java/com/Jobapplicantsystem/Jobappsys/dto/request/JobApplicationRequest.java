package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationRequest {
    private List<ApplicationAnswerRequest> answers;
}
