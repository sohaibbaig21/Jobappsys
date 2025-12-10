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
     * <p>
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
     * <p>
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
     * <p>
     * PURPOSE: Define security rules for all endpoints
     * <p>
     * IMPORTANT CONFIGURATION:
     * 1. Disable CSRF (not needed for stateless JWT authentication)
     * 2. Configure endpoint authorization rules
     * 3. Set session management to STATELESS (no server-side sessions)
     * 4. Add JWT filter before UsernamePasswordAuthenticationFilter
     * 5. Set exception handling entry point
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                // 1. Allow Frontend Files (HTML, CSS, JS)
                .requestMatchers("/", "/index.html", "/*.html", "/*.css", "/js/**").permitAll()

                // 2. Allow Auth APIs
                .requestMatchers("/api/auth/**").permitAll()

                // 3. Allow Public Job Search
                .requestMatchers("/api/jobs/**").permitAll()

                // 4. Protect Applicant-specific APIs
                .requestMatchers("/api/applicant/applications/**").hasRole("APPLICANT")

                // 5. Protect everything else
                .anyRequest().authenticated()
        );

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(authEntryPoint)
        );

        http.cors(cors -> {
        });

        return http.build();
    }
}
