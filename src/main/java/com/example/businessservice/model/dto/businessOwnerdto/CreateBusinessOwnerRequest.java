package com.example.businessservice.model.dto.businessOwnerdto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBusinessOwnerRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    private LocalDate dateOfBirth;

    private String education;

    private String skills;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Size(max = 100)
    private String email;

    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phone;

    @Size(max = 100, message = "Job title must be at most 100 characters")
    private String jobTitle;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    @Size(max = 255, message = "LinkedIn URL must be at most 255 characters")
    private String linkedInProfileUrl;

    private String bio;
}
