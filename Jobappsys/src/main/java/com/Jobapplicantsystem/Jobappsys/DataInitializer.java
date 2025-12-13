package com.Jobapplicantsystem.Jobappsys;

import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.model.JobPostQuestion;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostQuestionRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import com.Jobapplicantsystem.Jobappsys.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(JobPostRepository jobPostRepository, UserRepository userRepository, ApplicantRepository applicantRepository, PasswordEncoder passwordEncoder, JobPostQuestionRepository jobPostQuestionRepository) {
        return args -> {

            // Create and save a default user if not exists
            Optional<User> existingUser = userRepository.findByEmail("test@example.com");
            User defaultUser;
            if (existingUser.isEmpty()) {
                defaultUser = User.builder()
                        .firstName("Test")
                        .lastName("User")
                        .email("test@example.com")
                        .password(passwordEncoder.encode("password")) // Encode the password
                        .role(Role.APPLICANT) // Assign APPLICANT role
                        .build();
                userRepository.save(defaultUser);
                System.out.println("Default User Initialized!");
            } else {
                defaultUser = existingUser.get();
                System.out.println("Default User Already Exists!");
            }

            // Create and save a default applicant if not exists
            Optional<Applicant> existingApplicant = applicantRepository.findByUserId(defaultUser.getId());
            if (existingApplicant.isEmpty()) {
                Applicant defaultApplicant = Applicant.builder()
                        .user(defaultUser)
                        .build();
                applicantRepository.save(defaultApplicant);
                System.out.println("Default Applicant Initialized!");
            } else {
                System.out.println("Default Applicant Already Exists!");
            }

            // Create and save sample job posts
            JobPost job1 = JobPost.builder()
                    .title("Software Engineer")
                    .description("Develop and maintain software applications.")
                    .location("Remote")
                    .employmentType("Full-time")
                    .salary(80000.00)
                    .employer(defaultUser) // Link job posts to the default user as employer
                    .build();

            JobPost job2 = JobPost.builder()
                    .title("DevOps Engineer")
                    .description("Manage and improve our CI/CD pipelines.")
                    .location("New York")
                    .employmentType("Full-time")
                    .salary(95000.00)
                    .employer(defaultUser) // Link job posts to the default user as employer
                    .build();

            JobPost job3 = JobPost.builder()
                    .title("Frontend Developer")
                    .description("Build responsive user interfaces using React.")
                    .location("Remote")
                    .employmentType("Contract")
                    .salary(70000.00)
                    .employer(defaultUser) // Link job posts to the default user as employer
                    .build();

            jobPostRepository.save(job1);
            jobPostRepository.save(job2);
            jobPostRepository.save(job3);

            // Create and save sample job post questions
            JobPostQuestion question1_job1 = JobPostQuestion.builder()
                    .jobPost(job1)
                    .questionText("Why are you interested in this Software Engineer position?")
                    .questionType("TEXT")
                    .isRequired(true)
                    .questionOrder(1)
                    .build();

            JobPostQuestion question2_job1 = JobPostQuestion.builder()
                    .jobPost(job1)
                    .questionText("What is your preferred programming language?")
                    .questionType("SINGLE_CHOICE")
                    .options("Java,Python,JavaScript,C++")
                    .isRequired(true)
                    .questionOrder(2)
                    .build();

            JobPostQuestion question1_job2 = JobPostQuestion.builder()
                    .jobPost(job2)
                    .questionText("Describe your experience with CI/CD pipelines.")
                    .questionType("TEXT")
                    .isRequired(true)
                    .questionOrder(1)
                    .build();

            JobPostQuestion question1_job3 = JobPostQuestion.builder()
                    .jobPost(job3)
                    .questionText("Have you worked with React before?")
                    .questionType("SINGLE_CHOICE")
                    .options("Yes,No")
                    .isRequired(true)
                    .questionOrder(1)
                    .build();

            jobPostQuestionRepository.saveAll(List.of(question1_job1, question2_job1, question1_job2, question1_job3));

            System.out.println("Sample Job Posts and Questions Initialized!");
        };
    }
}
