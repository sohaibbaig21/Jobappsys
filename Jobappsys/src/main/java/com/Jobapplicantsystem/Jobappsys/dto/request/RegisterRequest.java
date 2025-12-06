package com.Jobapplicantsystem.Jobappsys.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;  // <--- You were missing this
    private String lastName;   // <--- You were missing this
    private String email;
    private String password;
    private String role;       // e.g., "EMPLOYER" or "APPLICANT"
}