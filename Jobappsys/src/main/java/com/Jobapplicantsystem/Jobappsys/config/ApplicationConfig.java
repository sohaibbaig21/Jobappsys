package com.Jobapplicantsystem.Jobappsys.config;

import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


// This file is reponsible for telling spring boot how to
// Load users from the database
//Verify passwords
//Handle login authentication



//@RequiredArgsConstructor due to lombok
//Automatically generates a constructor for all final fields


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    // userDetailsService() This creates a service that loads user data from the database
    //
    @Bean
    public UserDetailsService userDetailsService() {
        // Use our custom UserDetailsServiceImpl here
        return new com.Jobapplicantsystem.Jobappsys.service.auth.UserDetailsServiceImpl(userRepository);
    }

    // authenticationProvider() It Creates the authentication mechanism
   // by combining
    //UserDetailsService to find the user
    //PasswordEncoder  to verify passwords
    //
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
