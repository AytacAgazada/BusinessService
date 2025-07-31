package com.example.businessservice.service;

import com.example.businessservice.exception.BusinessOwnerAlreadyExistsException;
import com.example.businessservice.exception.ResourceNotFoundException;
import com.example.businessservice.feign.AuthServiceClient;
import com.example.businessservice.model.dto.businessOwnerdto.BusinessOwnerDto;
import com.example.businessservice.model.dto.businessOwnerdto.CreateBusinessOwnerRequest;
import com.example.businessservice.model.dto.businessOwnerdto.UpdateBusinessOwnerRequest;
import com.example.businessservice.model.entity.BusinessOwner;
import com.example.businessservice.repository.BusinessOwnerRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Loglama üçün əlavə edildi
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict; // Keşləmə üçün əlavə edildi
import org.springframework.cache.annotation.CachePut; // Keşləmə üçün əlavə edildi
import org.springframework.cache.annotation.Cacheable; // Keşləmə üçün əlavə edildi

import java.util.List;
import java.util.stream.Collectors; // List üçün əlavə edildi

@Service
@RequiredArgsConstructor
@Slf4j // Loglama üçün
@Transactional // Sinif səviyyəsində tranzaksiya idarəetməsi
public class BusinessOwnerService {

    private final BusinessOwnerRepository businessOwnerRepository; // 'final' açar sözü
    private final AuthServiceClient authServiceClient;

    @Cacheable(value = "businessOwners", key = "#authUserId")
    public BusinessOwnerDto createBusinessOwner(Long authUserId, CreateBusinessOwnerRequest request){
        log.info("Creating business owner with authUserId: {}", authUserId);

        try{
            String role = authServiceClient.getUserRole(authUserId);

            if (role == null || !role.equals("BUSINESS_OWNER")) {
                log.warn("Auth User ID {} does not have the BUSINESS_OWNER role. Current role: {}", authUserId, role);
                throw new ResourceNotFoundException("Auth User ID " + authUserId + " does not have the BUSINESS_OWNER role. BUSINESS_OWNER profile cannot be created.");
            }
        }catch (FeignException.NotFound e){
            log.warn("Auth User ID {} not found in authentication system.", authUserId);
            throw new ResourceNotFoundException("Auth User ID " + authUserId + " not found in authentication system.");
        }catch (FeignException e){
            log.error("Failed to communicate with authentication service during role verification for authUserId {}: {}", authUserId, e.getMessage());
            throw new RuntimeException("Failed to communicate with authentication service during role verification: " + e.getMessage(), e);
        }


        if (businessOwnerRepository.findByAuthUserId(authUserId).isPresent()) {
            log.warn("Business owner profile for Auth User ID {} already exists.", authUserId);
            throw new BusinessOwnerAlreadyExistsException("Business profile for Auth User ID " + authUserId + " already exists.");
        }

        if (businessOwnerRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Business profile for email {} already exists.", request.getEmail());
            throw new BusinessOwnerAlreadyExistsException("Business profile for email " + request.getEmail() + " already exists.");
        }

        BusinessOwner businessOwner = BusinessOwner.builder()
                .authUserId(authUserId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .education(request.getEducation())
                .skills(request.getSkills())
                .email(request.getEmail())
                .phone(request.getPhone())
                .jobTitle(request.getJobTitle())
                .yearsOfExperience(request.getYearsOfExperience())
                .linkedInProfileUrl(request.getLinkedInProfileUrl())
                .bio(request.getBio())
                .build();

        BusinessOwner savedBusinessOwner = businessOwnerRepository.save(businessOwner);
        log.info("Business owner created successfully with ID: {}", savedBusinessOwner.getId());
        return new BusinessOwnerDto(savedBusinessOwner);
    }

//    @Cacheable(value = "businessOwners", key = "'authUserId-' + #authUserId")
    public BusinessOwnerDto getBusinessOwnerByAuthUserId(Long authUserId) {
        log.info("Fetching business owner by Auth User ID: {}", authUserId);
        return businessOwnerRepository.findByAuthUserId(authUserId)
                .map(BusinessOwnerDto::new)
                .orElseThrow(() -> {
                    log.warn("Business profile for Auth User ID {} not found.", authUserId);
                    return new ResourceNotFoundException("Business profile for Auth User ID " + authUserId + " not found.");
                });
    }

    @Cacheable(value = "businessOwners", key = "#id")
    @Transactional(readOnly = true)
    public BusinessOwnerDto getBusinessOwnerById(Long id) {
        log.info("Fetching business owner by ID: {}", id);
        return businessOwnerRepository.findById(id)
                .map(BusinessOwnerDto::new)
                .orElseThrow(() -> {
                    log.warn("Business profile for ID {} not found.", id); // Loglama
                    return new ResourceNotFoundException("Business profile for ID " + id + " not found.");
                });
    }

    @Cacheable(value = "businessOwners")
    public List<BusinessOwnerDto> getAllBusinessOwners() {
        log.info("Fetching all business owners."); // Loglama
        return businessOwnerRepository.findAll()
                .stream()
                .map(BusinessOwnerDto::new)
                .collect(Collectors.toList());
    }

    @CachePut(value = "businessOwners", key = "#id")
    @Transactional
    public BusinessOwnerDto updateBusinessOwner(Long id, UpdateBusinessOwnerRequest request) {
        log.info("Updating business owner with ID: {}", id);
        BusinessOwner businessOwner = businessOwnerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Business profile for ID {} not found for update.", id);
                    return new ResourceNotFoundException("Business profile for ID " + id + " not found.");
                });


        if (request.getEmail() != null && !businessOwner.getEmail().equals(request.getEmail()) && businessOwnerRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Attempted to update business owner with ID {} to email {} which already exists.", id, request.getEmail());
            throw new BusinessOwnerAlreadyExistsException("Business profile for email " + request.getEmail() + " already exists.");
        }

        businessOwner.setFirstName(request.getFirstName());
        businessOwner.setLastName(request.getLastName());
        businessOwner.setDateOfBirth(request.getDateOfBirth());
        businessOwner.setEducation(request.getEducation());
        businessOwner.setSkills(request.getSkills());
        businessOwner.setEmail(request.getEmail());
        businessOwner.setPhone(request.getPhone());
        businessOwner.setJobTitle(request.getJobTitle());
        businessOwner.setYearsOfExperience(request.getYearsOfExperience());
        businessOwner.setLinkedInProfileUrl(request.getLinkedInProfileUrl());
        businessOwner.setBio(request.getBio());

        BusinessOwner updatedBusinessOwner = businessOwnerRepository.save(businessOwner);
        log.info("Business owner with ID {} updated successfully.", id);
        return new BusinessOwnerDto(updatedBusinessOwner);
    }

    @CacheEvict(value = "businessOwners", key = "#id")
    @Transactional
    public void deleteBusinessOwnerById(Long id) {
        log.info("Attempting to delete business owner with ID: {}", id);
        if (!businessOwnerRepository.existsById(id)) {
            log.warn("Business profile for ID {} not found for deletion.", id);
            throw new ResourceNotFoundException("Business profile for ID " + id + " not found.");
        }
        businessOwnerRepository.deleteById(id);
        log.info("Business owner with ID {} deleted successfully.", id);
    }

    @CacheEvict(value = "businessOwners", allEntries = true)
    @Transactional
    public void deleteAllBusinessOwners() {
        log.info("Clearing all business owners from DB and cache...");
        businessOwnerRepository.deleteAll();
    }
}