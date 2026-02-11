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
    Duration window, 
    
    @NotNull(message = "Algorithm is required")
    RateLimiterType algorithm, 
    
    @NotBlank(message = "Key pattern is required")
    String keyPattern
){
    // Compact constructor for Duration validation and string normalization
    public RateLimitRule {
        // Validate Duration is positive
        if (window != null && (window.isNegative() || window.isZero())) {
            throw new IllegalArgumentException("Window must be positive, got: " + window);
        }
        // for now we don't need this
        // // Normalize strings (trim whitespace)
        // if (id != null) {
        //     id = id.trim();
        // }
        // if (keyPattern != null) {
        //     keyPattern = keyPattern.trim();
        // }
    }
}
