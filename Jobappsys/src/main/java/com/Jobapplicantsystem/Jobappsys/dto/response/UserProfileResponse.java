package com.Jobapplicantsystem.Jobappsys.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role; // e.g., EMPLOYER, APPLICANT
    private String companyDescription;
}
