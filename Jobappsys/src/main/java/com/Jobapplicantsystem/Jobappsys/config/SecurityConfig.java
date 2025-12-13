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


//@Configuration tells Spring Boot to load this file
// when the application starts so settings are loaded


//@EnableWebSecurity It activates the Spring Security module the reason why security works

// @RequiredArgsConstructor due to Lombok
// it injects  jwtAuthFilter and authenticationProvider automatically. No boilerplate

// @Bean it makes a method available to whole project


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
                                "/jobs.html",
                                "/job.html",
                                "/profile-setup.html",

                                // Applicant  Pages
                                "/my-profile.html",
                                "/my-applications.html",

                                // Employer  Pages
                                "/employer-profile.html",
                                "/employer-dashboard.html",
                                "/post-job.html",
                                "/edit-job.html",
                                "/view-applicants.html",

                                // Static pages
                                "/styles.css",
                                "/favicon.ico",
                                "/js/**",
                                "/css/**",
                                "/images/**"
                        ).permitAll()

                        // PUBLIC API ENDPOINTS   no bearer tokens needed
                        .requestMatchers("/api/auth/**").permitAll()   // Login/Register
                        .requestMatchers("/api/jobs/**").permitAll()   // Viewing Jobs

                        //   PROTECTED API ENDPOINTS bearer tokens needed
                        // Accessing data requires a Token + Specific Role
                        .requestMatchers("/api/employer/**").hasRole("EMPLOYER")
                        .requestMatchers("/api/applicant/**").hasRole("APPLICANT")

                        // Downloads or File Access
                        .requestMatchers("/api/files/**").authenticated()

                        // CATCH-ALL
                        .anyRequest().authenticated()   //This protects all new APIs from being public
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