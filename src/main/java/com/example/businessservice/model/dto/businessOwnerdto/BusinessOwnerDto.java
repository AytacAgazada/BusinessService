package com.example.businessservice.model.dto.businessOwnerdto;

import com.example.businessservice.model.dto.businessDto.BusinessDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.example.businessservice.model.entity.BusinessOwner;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessOwnerDto {

    private Long id;
    private Long authUserId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String education;
    private String skills;
    private String email;
    private String phone;
    private String jobTitle;
    private Integer yearsOfExperience;
    private String linkedInProfileUrl;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<BusinessDto> businesses;

    public BusinessOwnerDto(BusinessOwner businessOwner) {
        this.id = businessOwner.getId();
        this.authUserId = businessOwner.getAuthUserId();
        this.firstName = businessOwner.getFirstName();
        this.lastName = businessOwner.getLastName();
        this.dateOfBirth = businessOwner.getDateOfBirth();
        this.education = businessOwner.getEducation();
        this.skills = businessOwner.getSkills();
        this.email = businessOwner.getEmail();
        this.phone = businessOwner.getPhone();
        this.jobTitle = businessOwner.getJobTitle();
        this.yearsOfExperience = businessOwner.getYearsOfExperience();
        this.linkedInProfileUrl = businessOwner.getLinkedInProfileUrl();
        this.bio = businessOwner.getBio();
    }
}
