package com.Jobapplicantsystem.Jobappsys.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * USER ENTITY - Base entity for all users in the system
 *
 * JAVA CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: This is the parent class for Applicant and Employer
 * 2. ENCAPSULATION: Private fields with public getters/setters (via Lombok)
 * 3. ABSTRACTION: Represents the abstract concept of a "User"
 * 4. POLYMORPHISM: UserType enum allows different user types
 *
 * JPA CONCEPTS:
 * - @Entity: Marks this class as a JPA entity (maps to database table)
 * - @Table: Specifies the table name in database
 * - @Id: Marks the primary key field
 * - @GeneratedValue: Auto-generates UUID values
 * - @Column: Maps field to database column with constraints
 * - @Enumerated: Maps Java enum to database column
 *
 * LOMBOK ANNOTATIONS:
 * - @Data: Generates getters, setters, toString, equals, hashCode
 * - @Builder: Implements Builder pattern for object creation
 * - @NoArgsConstructor: Creates no-argument constructor (required by JPA)
 * - @AllArgsConstructor: Creates constructor with all arguments
 */
@Entity
@Table(name = "users")
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@Builder // Lombok: Implements Builder design pattern for object construction
@NoArgsConstructor // Lombok: Required by JPA for entity instantiation
@AllArgsConstructor // Lombok: Constructor with all fields
public class User {

    /**
     * PRIMARY KEY
     * UUID (Universally Unique Identifier) is used instead of auto-increment Long
     *
     * ADVANTAGES OF UUID:
     * 1. Globally unique (can merge databases without conflicts)
     * 2. No sequential pattern (better security)
     * 3. Can generate on client side before database insert
     * 4. Works well in distributed systems
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    /**
     * EMAIL - Unique identifier for user authentication
     *
     * VALIDATION ANNOTATIONS:
     * @NotBlank: Ensures field is not null, empty, or whitespace
     * @Email: Validates email format
     * @Size: Restricts string length
     *
     * COLUMN CONSTRAINTS:
     * unique = true: Database-level constraint (one email per user)
     * nullable = false: Cannot be null
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must be less than 255 characters")
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    /**
     * PASSWORD - Stored as hashed value (never plain text)
     *
     * SECURITY CONCEPT: Password Hashing
     * - Plain text passwords are NEVER stored
     * - We use BCrypt algorithm to create one-way hash
     * - Even if database is compromised, passwords remain secure
     * - BCrypt includes salt (random data) to prevent rainbow table attacks
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * CONTACT INFORMATION
     * Optional fields for user profile
     */
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Size(max = 100, message = "City must be less than 100 characters")
    @Column(name = "city", length = 100)
    private String city;

    @Size(max = 100, message = "Country must be less than 100 characters")
    @Column(name = "country", length = 100)
    private String country;

    /**
     * USER TYPE - Discriminator for different user roles
     *
     * POLYMORPHISM CONCEPT:
     * - UserType enum allows single Users table to represent different types
     * - This is an alternative to Table-Per-Class inheritance strategy
     * - Strategy Pattern: Different behavior based on user type
     *
     * @Enumerated(EnumType.STRING):
     * - Stores enum as string in database (e.g., "APPLICANT", "EMPLOYER")
     * - Alternative is EnumType.ORDINAL (stores as integer 0, 1, 2...)
     * - STRING is preferred: more readable and robust to enum reordering
     */
    @NotBlank(message = "User type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 20)
    private UserType userType;

    /**
     * AUDIT FIELDS - Track when records are created/modified
     *
     * TEMPORAL DATA CONCEPT:
     * - Maintains historical information
     * - Useful for debugging and compliance
     *
     * @CreationTimestamp: Automatically sets timestamp when entity is created
     * @UpdateTimestamp: Automatically updates timestamp when entity is modified
     *
     * updatable = false: createdAt should never change after initial creation
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * SOFT DELETE FLAG
     *
     * SOFT DELETE PATTERN:
     * - Instead of deleting records (hard delete), we mark them as inactive
     * - Preserves referential integrity and audit trail
     * - Allows "undelete" functionality
     * - Queries should filter by isActive = true
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default // Lombok: Sets default value in builder
    private Boolean isActive = true;

    /**
     * HELPER METHODS
     * Business logic methods for User entity
     */

    /**
     * Checks if user is an applicant
     * @return true if user type is APPLICANT
     */
    public boolean isApplicant() {
        return this.userType == UserType.APPLICANT;
    }

    /**
     * Checks if user is an employer
     * @return true if user type is EMPLOYER
     */
    public boolean isEmployer() {
        return this.userType == UserType.EMPLOYER;
    }

    /**
     * Masks email for logging/display (privacy protection)
     * Example: john.doe@example.com -> j***e@example.com
     *
     * @return masked email string
     */
    public String getMaskedEmail() {
        if (email == null || email.length() < 3) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return email.charAt(0) + "***" + email.substring(atIndex);
        }
        return email.charAt(0) + "***" + email.charAt(atIndex - 1) + email.substring(atIndex);
    }

    /**
     * INNER ENUM: UserType
     *
     * ENUM CONCEPT:
     * - Type-safe way to represent fixed set of constants
     * - Better than using strings or integers
     * - Compile-time checking prevents invalid values
     * - Can have methods and fields
     */
    public enum UserType {
        APPLICANT("Job Seeker"),
        EMPLOYER("Hiring Manager");

        private final String displayName;

        /**
         * ENUM CONSTRUCTOR
         * Enums can have constructors (always private)
         */
        UserType(String displayName) {
            this.displayName = displayName;
        }

        /**
         * Gets user-friendly display name
         * @return display name for the user type
         */
        public String getDisplayName() {
            return displayName;
        }
    }
}

/**
 * ============================================
 * JAVA CONCEPTS SUMMARY
 * ============================================
 *
 * 1. OOP PRINCIPLES:
 *    - Encapsulation: Private fields with controlled access
 *    - Inheritance: Base class for Applicant and Employer
 *    - Polymorphism: UserType enum for different behaviors
 *    - Abstraction: Represents the concept of a user
 *
 * 2. DESIGN PATTERNS:
 *    - Builder Pattern: @Builder annotation
 *    - Factory Pattern: Enum constructors
 *    - Strategy Pattern: Different behavior based on UserType
 *
 * 3. BEST PRACTICES:
 *    - Immutable primary key (updatable = false)
 *    - Audit fields for tracking changes
 *    - Soft delete instead of hard delete
 *    - UUID for distributed systems
 *    - Password hashing for security
 *    - Bean Validation annotations
 *
 * 4. JPA/HIBERNATE:
 *    - Entity mapping with @Entity
 *    - Column constraints and validation
 *    - Automatic timestamp management
 *    - UUID generation strategy
 *
 * 5. LOMBOK:
 *    - Reduces boilerplate code
 *    - @Data for common methods
 *    - @Builder for fluent API
 *    - @NoArgsConstructor for JPA
 *
 * ============================================
 * USAGE EXAMPLES
 * ============================================
 *
 * // Creating a new user with Builder pattern
 * User user = User.builder()
 *     .email("john.doe@example.com")
 *     .passwordHash(hashedPassword)
 *     .userType(User.UserType.APPLICANT)
 *     .phoneNumber("+1234567890")
 *     .city("New York")
 *     .country("USA")
 *     .build();
 *
 * // Checking user type (Polymorphism)
 * if (user.isApplicant()) {
 *     // Handle applicant-specific logic
 * }
 *
 * // Soft delete
 * user.setIsActive(false);
 *
 * // Get masked email for logging
 * String masked = user.getMaskedEmail();
 */