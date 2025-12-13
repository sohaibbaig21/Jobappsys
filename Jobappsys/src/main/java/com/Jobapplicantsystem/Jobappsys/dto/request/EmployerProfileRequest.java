package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.Data;

@Data
public class EmployerProfileRequest {
    private String companyName; // required
    private String companyDescription;
    private String companyWebsite;
    private String companySize;
    private String industry;
    private String logoUrl;
}
