package com.Jobapplicantsystem.Jobappsys.controller;

import com.Jobapplicantsystem.Jobappsys.dto.response.UserProfileResponse;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        var userOpt = userRepository.findByEmail(principal.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        var u = userOpt.get();
        var dto = UserProfileResponse.builder()
                .id(u.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .role(u.getRole() != null ? u.getRole().name() : null)
                .companyDescription(u.getCompanyDescription())
                .build();
        return ResponseEntity.ok(dto);
    }
}
