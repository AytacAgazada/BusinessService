package com.example.businessservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "business-auth-service", url = "${business-auth-service.url}")
public interface AuthServiceClient {

    @GetMapping("/api/auth/{authUserId}/exists")
    Boolean doesUserExist(@PathVariable("authUserId") Long authUserId);

    @GetMapping("/api/auth/{authUserId}/role")
    String getUserRole(@PathVariable("authUserId") Long authUserId);
}
