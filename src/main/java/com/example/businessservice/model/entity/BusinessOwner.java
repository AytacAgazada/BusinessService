package com.example.businessservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; // Getter/Setter, toString, equals, hashCode üçün
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "business_owner_profiles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BusinessOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long authUserId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = true)
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "TEXT")
    private String education;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String jobTitle;

    @Column
    private Integer yearsOfExperience;

    @Column(length = 255)
    private String linkedInProfileUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Lombok @Builder istifadə edərkən boş kolleksiya yaratmaq üçün
    private Set<Business> businesses = new HashSet<>();


    public void addBusiness(Business business) {
        businesses.add(business);
        business.setOwner(this);
    }

    public void removeBusiness(Business business) {
        businesses.remove(business);
        business.setOwner(null);
    }
}

