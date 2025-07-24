package com.example.businessservice.model.dto.businessDto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBusinessRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name must be at most 100 characters")
    private String companyName;

    @NotBlank(message = "Business type is required")
    @Size(max = 100, message = "Business type must be at most 100 characters")
    private String businessType;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @Size(max = 255, message = "Website must be at most 255 characters")
    private String website;

    @Email(message = "Email must be valid")
    @Size(max = 255)
    private String email;

    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phone;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;
}
