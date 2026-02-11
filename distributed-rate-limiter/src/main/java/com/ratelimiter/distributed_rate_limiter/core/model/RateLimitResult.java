package com.ratelimiter.distributed_rate_limiter.core.model;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RateLimitResult(
    @NotNull(message = "Allowed is required")
    boolean allowed,
    
    @Positive(message = "Remaining must be greater than 0")
    int remaining,
    
    long retryAfterMs,

    Map<String, String> headers
) {
    // In this we will be using the static factory methods to create the instance
    public static RateLimitResult allowed(int remaining, int limit, long resetEpochSeconds) {
        Map<String, String> headers = Map.of(
            "X-RateLimit-Limit", String.valueOf(limit),
            "X-RateLimit-Remaining", String.valueOf(remaining),
            "X-RateLimit-Reset", String.valueOf(resetEpochSeconds)
        );
        return new RateLimitResult(true, remaining, 0L, headers);
    }

    public static RateLimitResult denied(int limit, long retryAfterMs) {
        Map<String, String> headers = Map.of(
            "X-RateLimit-Limit", String.valueOf(limit),
            "X-RateLimit-Remaining", "0",
            "Retry-After", String.valueOf(retryAfterMs / 1000)
        );
        return new RateLimitResult(false, 0, retryAfterMs, headers);
    }

}