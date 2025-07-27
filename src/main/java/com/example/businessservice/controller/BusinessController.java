package com.example.businessservice.controller;


import com.example.businessservice.model.dto.businessDto.BusinessDto;
import com.example.businessservice.model.dto.businessDto.CreateBusinessRequest;
import com.example.businessservice.model.dto.businessDto.UpdateBusinessRequest;
import com.example.businessservice.service.BusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/businesses")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<BusinessDto> createBusiness(
            @Valid @RequestBody CreateBusinessRequest request) {
        log.info("Received request to create business with company name: {}", request.getCompanyName());
        log.debug("CreateBusinessRequest details: {}", request);

        BusinessDto createdBusiness = businessService.createBusiness(request.getOwnerId(), request);

        log.info("Business created successfully with ID: {}", createdBusiness.getId());
        return new ResponseEntity<>(createdBusiness, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDto> getBusinessById(@PathVariable Long id) {
        log.info("Received request to fetch business by ID: {}", id);

        BusinessDto businessDto = businessService.getBusinessById(id);

        log.info("Successfully fetched business with ID: {}", id);
        return ResponseEntity.ok(businessDto);
    }

    @GetMapping("/by-company/{companyName}")
    public ResponseEntity<BusinessDto> getBusinessByCompanyName(@PathVariable String companyName) {
        log.info("Received request to fetch business by company name: {}", companyName);

        BusinessDto businessDto = businessService.getBusinessByCompanyName(companyName);

        log.info("Successfully fetched business with company name: {}", companyName);
        return ResponseEntity.ok(businessDto);
    }

    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<List<BusinessDto>> getBusinessesByOwnerId(@PathVariable Long ownerId) {
        log.info("Received request to fetch businesses by owner ID: {}", ownerId);

        List<BusinessDto> businesses = businessService.getBusinessesByOwnerId(ownerId);

        log.info("Successfully fetched {} businesses for owner ID: {}", businesses.size(), ownerId);
        return ResponseEntity.ok(businesses);
    }

    @GetMapping
    public ResponseEntity<List<BusinessDto>> getAllBusinesses() {
        log.info("Received request to fetch all businesses.");

        List<BusinessDto> businesses = businessService.getAllBusinesses();

        log.info("Successfully fetched {} businesses.", businesses.size());
        return ResponseEntity.ok(businesses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessDto> updateBusiness(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBusinessRequest request) {
        log.info("Received request to update business with ID: {}", id);
        log.debug("UpdateBusinessRequest details for ID {}: {}", id, request);

        BusinessDto updatedBusiness = businessService.updateBusiness(id, request);

        log.info("Business with ID: {} updated successfully.", id);
        return ResponseEntity.ok(updatedBusiness);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessById(@PathVariable Long id) {
        log.info("Received request to delete business with ID: {}", id);

        businessService.deleteBusinessById(id);

        log.info("Business with ID: {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllBusinesses() {
        log.warn("Received request to delete ALL businesses. This operation should be used with caution!");

        businessService.deleteAllBusinesses();

        log.info("All businesses deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> doesBusinessExistById(@PathVariable Long id) {
        log.info("Checking if customer exists with Auth User ID: {}",id);
        boolean exists = businessService.doesBusinessExistById(id);
        return ResponseEntity.ok(exists);
    }
}