package com.example.businessservice.repository;

import com.example.businessservice.model.entity.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Long> {

    Optional<BusinessOwner> findByAuthUserId(Long authUserId);

    Optional<BusinessOwner> findByEmail(String email);

}
