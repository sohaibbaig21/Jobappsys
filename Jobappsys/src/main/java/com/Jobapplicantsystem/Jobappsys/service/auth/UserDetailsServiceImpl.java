package com.Jobapplicantsystem.Jobappsys.service.auth;

import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * UserDetailsServiceImpl
 *
 * Loads user by username (email) from DB and converts to Spring Security UserDetails.
 *
 * Assumption: User entity has methods getEmail(), getPassword(), and getRole() (returning a String or enum).
 * If your User entity uses different field names, adapt the mapping accordingly.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Map role to GrantedAuthority (assumes userType like "APPLICANT" or "EMPLOYER")
        String role = user.getUserType() != null ? user.getUserType().name() : "USER";
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.getIsActive() != null ? !user.getIsActive() : false) // if you have isActive
                .build();
    }
}
