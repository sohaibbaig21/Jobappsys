package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostRequest {
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private Double salary;

    private List<QuestionDto> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDto {
        private String questionText;
        private String questionType;
        private Boolean isRequired;
        private Integer questionOrder;
        private List<String> options;
    }
}
