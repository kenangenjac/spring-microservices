package com.cloud.microservices.currencyexchangeservice.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {
    private final  Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @Retry(name = "sample-api", fallbackMethod = "fallback")
    @CircuitBreaker(name = "default", fallbackMethod = "fallback")
    @RateLimiter(name = "default")
    @Bulkhead(name = "default")
    @GetMapping("/sample-api")
    public String sampleApi() {
        logger.info("Sample call received");
        return new RestTemplate().getForEntity("http://localhost:9999", String.class).getBody();
    }

    private String fallback(Exception e) {
        return "Fallback";
    }
}
