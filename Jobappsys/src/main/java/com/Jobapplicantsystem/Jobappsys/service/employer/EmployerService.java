package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.response.AuthResponse;

public interface EmployerService {
    AuthResponse getEmployerProfile(Long employerId);
    void updateEmployerProfile(Long employerId, AuthResponse profile);
}
