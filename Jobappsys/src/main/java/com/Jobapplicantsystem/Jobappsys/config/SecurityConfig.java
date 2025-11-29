// ============================================
// FILE 2: SecurityConfig.java
// Location: src/main/java/com/Jobapplicantsystem/config/SecurityConfig.java
// ============================================

package com.Jobapplicantsystem.Jobappsys.config;

import com.Jobapplicantsystem.Jobappsys.security.JwtAuthenticationEntryPoint;
import com.Jobapplicantsystem.Jobappsys.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Spring Security Configuration
 *
 * PURPOSE: Configure authentication, authorization, and security filters
 *
 * KEY CONCEPTS:
 * 1. JWT Authentication - Stateless authentication using tokens
 * 2. BCrypt Password Encoding - One-way hashing for passwords
 * 3. Security Filter Chain - Intercepts requests for security checks
 * 4. Authorization - Role-based access control (RBAC)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Lombok: Constructor injection
public class SecurityConfig {

    // TODO: Inject JwtAuthenticationFilter (custom filter to validate JWT)
    private final JwtAuthenticationFilter jwtAuthFilter;

    // TODO: Inject JwtAuthenticationEntryPoint (handles unauthorized access)
    private final JwtAuthenticationEntryPoint authEntryPoint;

    /**
     * PASSWORD ENCODER BEAN
     *
     * JAVA CONCEPT: Bean Definition
     * - @Bean methods create Spring-managed objects
     * - BCryptPasswordEncoder uses BCrypt hashing algorithm
     * - Includes automatic salt generation
     * - Cost factor = 10 (default, higher = more secure but slower)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO: Return new BCryptPasswordEncoder()
        // This encoder hashes passwords with BCrypt algorithm
        // NEVER store plain text passwords!
        return new BCryptPasswordEncoder();
    }

    /**
     * AUTHENTICATION MANAGER BEAN
     *
     * PURPOSE: Manages authentication process
     * - Used in login to verify credentials
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        // TODO: Return authConfig.getAuthenticationManager()
        return authConfig.getAuthenticationManager();
    }

    /**
     * SECURITY FILTER CHAIN
     *
     * PURPOSE: Define security rules for all endpoints
     *
     * IMPORTANT CONFIGURATION:
     * 1. Disable CSRF (not needed for stateless JWT authentication)
     * 2. Configure endpoint authorization rules
     * 3. Set session management to STATELESS (no server-side sessions)
     * 4. Add JWT filter before UsernamePasswordAuthenticationFilter
     * 5. Set exception handling entry point
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // STEP 1: Disable CSRF (Cross-Site Request Forgery)
        // - Not needed for JWT because tokens are in headers, not cookies
        http.csrf(csrf -> csrf.disable());

        // STEP 2: Configure Authorization Rules
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()  // Public: login, register
                .requestMatchers("/api/jobs/search/**").permitAll()  // Public: job search
                .requestMatchers("/api/applicant/**").hasAuthority("APPLICANT")  // Applicant only
                .requestMatchers("/api/employer/**").hasAuthority("EMPLOYER")  // Employer only
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Public: API docs
                .anyRequest().authenticated()  // All other endpoints require authentication
        );

        // STEP 3: Set Session Management to STATELESS
        // - No server-side sessions (JWT is stateless)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // STEP 4: Add JWT Filter
        // - Custom filter to extract and validate JWT from requests
        // - Must run BEFORE UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // STEP 5: Exception Handling
        // - Handle unauthorized access (401) and forbidden access (403)
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(authEntryPoint)
        );

        // STEP 6: Enable CORS
        http.cors(cors -> {});

        // TODO: Return http.build()
        return http.build();
    }
}
