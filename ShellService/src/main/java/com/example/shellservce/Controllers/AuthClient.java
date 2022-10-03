package com.example.shellservce.Controllers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient("auth-service")
public interface AuthClient {
    @PostMapping("api/v1/auth/validate")
    ResponseEntity isFraudster(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

}
