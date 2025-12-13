package com.Jobapplicantsystem.Jobappsys.config;

import com.Jobapplicantsystem.Jobappsys.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 👇 1. MANUAL WHITELIST OF ALL STATIC PAGES
                        // We permit these because static HTML cannot send Auth headers on initial load.
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",
                                "/jobs.html",               // Public Job Board
                                "/job.html",                // Single Job Details
                                // "/profile.html",         // Removed: generic profile page no longer used
                                "/profile-setup.html",      // Setup Page

                                // Applicant Specific Pages (Frontend Logic handles security)
                                "/my-profile.html",
                                "/my-applications.html",

                                // Employer Specific Pages (Frontend Logic handles security)
                                "/employer-profile.html",
                                "/employer-dashboard.html",
                                "/post-job.html",
                                "/edit-job.html",
                                "/view-applicants.html",

                                // Static Assets (CSS, JS, Images)
                                "/styles.css",
                                "/favicon.ico",
                                "/js/**",
                                "/css/**",
                                "/images/**"
                        ).permitAll()

                        // 👇 2. PUBLIC API ENDPOINTS
                        .requestMatchers("/api/auth/**").permitAll()   // Login/Register
                        .requestMatchers("/api/jobs/**").permitAll()   // Viewing Jobs

                        // 👇 3. PROTECTED API ENDPOINTS (The Real Security)
                        // Accessing data requires a Token + Specific Role
                        .requestMatchers("/api/employer/**").hasRole("EMPLOYER")
                        .requestMatchers("/api/applicant/**").hasRole("APPLICANT")

                        // Downloads or File Access
                        .requestMatchers("/api/files/**").authenticated()

                        // 👇 4. CATCH-ALL
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("http://localhost:*")); // Allow frontend access
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}