
package com.Jobapplicantsystem.Jobappsys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// allows the React frontend (e.g., http://localhost:3000) to make API calls
// to the Spring Boot backend (http://localhost:8080).
// Normally, browsers enforce the Same-Origin Policy, which blocks requests from different
// domains or ports. By setting appropriate CORS headers,
// the backend tells the browser which origins are allowed,
// enabling safe cross-origin communication.
@Configuration
public class CorsConfig implements WebMvcConfigurer {    //Helps in CORS configuration

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



        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
