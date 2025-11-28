// ============================================
// FILE 1: CorsConfig.java
// Location: src/main/java/com/Jobapplicantsystem/config/CorsConfig.java
// ============================================

package com.Jobapplicantsystem.Jobappsys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS (Cross-Origin Resource Sharing) Configuration
 *
 * PURPOSE: Allows React frontend (http://localhost:3000) to make API calls
 * to Spring Boot backend (http://localhost:8080)
 *
 * KEY CONCEPT: Same-Origin Policy
 * - Browsers block requests from different origins (domain/port) by default
 * - CORS headers tell browser which origins are allowed
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO: Configure CORS for all API endpoints ("/api/**")

        // REQUIRED CONFIGURATION:
        // 1. addMapping("/api/**") - Apply to all API routes
        // 2. allowedOrigins("http://localhost:3000") - React dev server
        // 3. allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") - All HTTP methods
        // 4. allowedHeaders("*") - Allow all headers
        // 5. allowCredentials(true) - Allow cookies/auth headers
        // 6. maxAge(3600) - Cache preflight response for 1 hour

        // PRODUCTION NOTE:
        // Replace "http://localhost:3000" with actual frontend domain
        // Example: "https://yourapp.com"

        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
