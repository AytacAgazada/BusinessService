package com.example.businessservice.service;

import com.example.businessservice.exception.BusinessAlreadyExistsException;
import com.example.businessservice.exception.ResourceNotFoundException;
import com.example.businessservice.model.dto.businessDto.BusinessDto;
import com.example.businessservice.model.dto.businessDto.CreateBusinessRequest;
import com.example.businessservice.model.dto.businessDto.UpdateBusinessRequest;
import com.example.businessservice.model.entity.Business;
import com.example.businessservice.repository.BusinessOwnerRepository;
import com.example.businessservice.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessOwnerRepository businessOwnerRepository;


    @Caching(
            evict = {
                    @CacheEvict(value = "businessById", allEntries = true),
                    @CacheEvict(value = "businessByCompanyName", allEntries = true),
                    @CacheEvict(value = "allBusinessesCache", allEntries = true),
                    @CacheEvict(value = "businessesByOwnerId", allEntries = true)
            }
    )
    public BusinessDto createBusiness(Long ownerId, CreateBusinessRequest request) {
        log.info("Attempting to create business for ownerId: {}", ownerId);
        if (businessRepository.findByCompanyName(request.getCompanyName()).isPresent()) {
            log.warn("Business with company name {} already exists for ownerId: {}", request.getCompanyName(), ownerId);
            throw new RuntimeException("Business with company name " + request.getCompanyName() + " already exists for ownerId: " + ownerId);
        }

        Business business = Business.builder()
                .owner(businessOwnerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Business owner not found with ID: " + ownerId)))
                .companyName(request.getCompanyName())
                .businessType(request.getBusinessType())
                .description(request.getDescription())
                .website(request.getWebsite())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        Business savedBusiness = businessRepository.save(business);
        log.info("Business created successfully with ID: {} for ownerId: {}", savedBusiness.getId(), ownerId);
        return new BusinessDto(savedBusiness);
    }

    public BusinessDto getBusinessById(Long id) {
        log.info("Fetching business by ID: {}", id);
        return businessRepository.findById(id)
                .map(BusinessDto::new)
                .orElseThrow(() -> {
                    log.warn("Business with ID {} not found.", id);
                    return new ResourceNotFoundException("Business not found with ID: " + id);
                });
    }

    public BusinessDto getBusinessByCompanyName(String companyName) {
        log.info("Fetching business by company name: {}", companyName);
        return businessRepository.findByCompanyName(companyName)
                .map(BusinessDto::new)
                .orElseThrow(() -> {
                    log.warn("Business with company name '{}' not found.", companyName);
                    return new ResourceNotFoundException("Business not found with company name: " + companyName);
                });
    }

    @Cacheable(value = "allBusinessesCache")
    public List<BusinessDto> getAllBusinesses() {
        log.info("Fetching all businesses.");
        return businessRepository.findAll()
                .stream()
                .map(BusinessDto::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "businessesByOwnerId", key = "#ownerId")
    public List<BusinessDto> getBusinessesByOwnerId(Long ownerId) {
        log.info("Fetching businesses by owner ID: {}", ownerId);
        List<Business> businesses = businessRepository.findByOwnerId(ownerId);
        if (businesses.isEmpty()) {
            log.warn("No businesses found for owner ID: {}", ownerId);
        }
        return businesses.stream()
                .map(BusinessDto::new)
                .collect(Collectors.toList());
    }

    @Caching(
            put = {
                    @CachePut(value = "businessById", key = "#id"),
                    @CachePut(value = "businessByCompanyName", key = "#result.companyName"),
            },
            evict = {
                    @CacheEvict(value = "allBusinessesCache", allEntries = true)
            }
    )

    public BusinessDto updateBusiness(Long id, UpdateBusinessRequest request) {
        log.info("Attempting to update business with ID: {}", id);
        Business existingBusiness = businessRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Business with ID {} not found for update operation.", id);
                    return new ResourceNotFoundException("Business not found with ID: " + id);
                });

        if (request.getCompanyName() != null && !existingBusiness.getCompanyName().equals(request.getCompanyName()) && businessRepository.existsByCompanyName(request.getCompanyName())) {
            log.warn("Company name '{}' is already taken by another business (ID: {}).", request.getCompanyName(), id);
            throw new BusinessAlreadyExistsException("Company name " + request.getCompanyName() + " is already taken by another business.");
        }

        if (request.getCompanyName() != null) existingBusiness.setCompanyName(request.getCompanyName());
        if (request.getBusinessType() != null) existingBusiness.setBusinessType(request.getBusinessType());
        if (request.getDescription() != null) existingBusiness.setDescription(request.getDescription());
        if (request.getWebsite() != null) existingBusiness.setWebsite(request.getWebsite());
        if (request.getEmail() != null) existingBusiness.setEmail(request.getEmail());
        if (request.getPhone() != null) existingBusiness.setPhone(request.getPhone());
        if (request.getAddress() != null) existingBusiness.setAddress(request.getAddress());

        Business updatedBusiness = businessRepository.save(existingBusiness);
        log.info("Business with ID {} updated successfully.", id);
        return new BusinessDto(updatedBusiness);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "businessById", key = "#id"),
                    @CacheEvict(value = "allBusinessesCache", allEntries = true),
            }
    )
    public void deleteBusinessById(Long id) {
        log.info("Attempting to delete business with ID: {}", id);
        if (!businessRepository.existsById(id)) {
            log.warn("Business with ID {} not found for deletion operation.", id);
            throw new ResourceNotFoundException("Business not found with ID: " + id);
        }
        businessRepository.deleteById(id);
        log.info("Business with ID {} deleted successfully.", id);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "businessById", allEntries = true),
                    @CacheEvict(value = "businessByCompanyName", allEntries = true),
                    @CacheEvict(value = "allBusinessesCache", allEntries = true),
                    @CacheEvict(value = "businessesByOwnerId", allEntries = true)
            }
    )
    public void deleteAllBusinesses() {
        log.info("Clearing all businesses from DB and cache...");
        businessRepository.deleteAll();
    }

    public boolean doesBusinessExistById(Long id) {
        return businessRepository.existsById(id);
    }
}