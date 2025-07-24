package com.example.businessservice.model.dto.businessDto;

import com.example.businessservice.model.entity.Business;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class BusinessDto {

    private Long id;
    private String companyName;
    private String businessType;
    private String description;
    private String website;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long ownerId;

    public BusinessDto(Business business) {
        this.id = business.getId();
        this.companyName = business.getCompanyName();
        this.businessType = business.getBusinessType();
        this.description = business.getDescription();
        this.website = business.getWebsite();
        this.email = business.getEmail();
        this.phone = business.getPhone();
        this.address = business.getAddress();
        this.createdAt = business.getCreatedAt();
    }
}
