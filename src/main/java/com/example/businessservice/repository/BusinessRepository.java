package com.example.businessservice.repository;

import com.example.businessservice.model.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByCompanyName(String companyName);

    List<Business> findByOwnerId(Long ownerId);

    boolean existsByCompanyName(String companyName);


}

