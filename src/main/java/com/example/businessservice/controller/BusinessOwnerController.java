package com.example.businessservice.controller;

import com.example.businessservice.model.dto.businessOwnerdto.BusinessOwnerDto;
import com.example.businessservice.model.dto.businessOwnerdto.CreateBusinessOwnerRequest;
import com.example.businessservice.model.dto.businessOwnerdto.UpdateBusinessOwnerRequest;
import com.example.businessservice.service.BusinessOwnerService;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/business-owners")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BusinessOwnerController {

    private final BusinessOwnerService businessOwnerService;

    @PostMapping
    public ResponseEntity<BusinessOwnerDto> createBusinessOwner(
            @RequestHeader("X-Auth-User-Id") Long authUserId,
            @Valid @RequestBody CreateBusinessOwnerRequest request) {
        log.info("Received request to create business owner profile for Auth User ID: {}", authUserId);
        BusinessOwnerDto createdOwner = businessOwnerService.createBusinessOwner(authUserId, request);
        log.info("Business owner profile created successfully with ID: {} for Auth User ID: {}", createdOwner.getId(), authUserId);
        return new ResponseEntity<>(createdOwner, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessOwnerDto> getBusinessOwnerById(@PathVariable Long id) {
        log.info("Received request to fetch business owner profile by ID: {}", id);

        BusinessOwnerDto ownerDto = businessOwnerService.getBusinessOwnerById(id);

        log.info("Successfully fetched business owner profile with ID: {}", id);
        return ResponseEntity.ok(ownerDto);
    }

    @GetMapping("/by-auth/{authUserId}")
    public ResponseEntity<BusinessOwnerDto> getBusinessOwnerByAuthUserId(@PathVariable Long authUserId) {
        log.info("Received request to fetch business owner profile by Auth User ID: {}", authUserId);

        BusinessOwnerDto ownerDto = businessOwnerService.getBusinessOwnerByAuthUserId(authUserId);

        log.info("Successfully fetched business owner profile for Auth User ID: {}", authUserId);
        return ResponseEntity.ok(ownerDto);
    }

    @GetMapping
    public ResponseEntity<List<BusinessOwnerDto>> getAllBusinessOwners() {
        log.info("Received request to fetch all business owner profiles.");

        List<BusinessOwnerDto> owners = businessOwnerService.getAllBusinessOwners();

        log.info("Successfully fetched {} business owner profiles.", owners.size());
        return ResponseEntity.ok(owners);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessOwnerDto> updateBusinessOwner(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBusinessOwnerRequest request) {
        log.info("Received request to update business owner profile with ID: {}", id);
        log.debug("UpdateBusinessOwnerRequest details for ID {}: {}", id, request);

        BusinessOwnerDto updatedOwner = businessOwnerService.updateBusinessOwner(id, request);

        log.info("Business owner profile with ID: {} updated successfully.", id);
        return ResponseEntity.ok(updatedOwner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessOwnerById(@PathVariable Long id) {
        log.info("Received request to delete business owner profile with ID: {}", id);

        businessOwnerService.deleteBusinessOwnerById(id);

        log.info("Business owner profile with ID: {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllBusinessOwners() {
        log.warn("Received request to delete ALL business owner profiles. This operation should be used with caution!");

        businessOwnerService.deleteAllBusinessOwners();

        log.info("All business owner profiles deleted successfully.");
        return ResponseEntity.noContent().build();
    }
}