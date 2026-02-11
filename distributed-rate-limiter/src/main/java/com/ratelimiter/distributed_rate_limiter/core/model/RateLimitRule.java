package com.ratelimiter.distributed_rate_limiter.core.model;

import java.time.Duration;

import com.ratelimiter.distributed_rate_limiter.core.algorithm.enums.RateLimiterType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


// record automatically provides everything the class does like
// getters, setters, constructors, equals, hashCode, toString
public record RateLimitRule(
    @NotBlank(message = "ID is required")
    String id, 
    @Positive(message = "Max requests must be greater than 0")
    int maxRequests, 
    @NotNull(message = "Window is required")
    @Positive(message = "Window must be greater than 0")
    Duration window, 
    @NotNull(message = "Algorithm is required")
    RateLimiterType algorithm, 
    @NotBlank(message = "Key pattern is required")
    String keyPattern
){}
