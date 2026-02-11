package com.ratelimiter.distributed_rate_limiter.core.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ratelimiter.distributed_rate_limiter.core.algorithm.RateLimiter;
import com.ratelimiter.distributed_rate_limiter.core.algorithm.enums.RateLimiterType;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitResult;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RateLimiterService {
    private final Map<RateLimiterType, RateLimiter> rateLimiters;

    public RateLimiterService(List<RateLimiter> rateLimiterList) {
        this.rateLimiters = rateLimiterList.stream()
            .collect(Collectors.toMap(RateLimiter::getType, Function.identity()));

        log.info("RateLimiterService initialized with {} rate limiters", rateLimiters.size());
    }

    public RateLimitResult tryAcquire(String key, RateLimitRule rule) {
        // Get the appropriate rate limiter based on rule's algorithm type
        RateLimiter limiter = rateLimiters.get(rule.algorithm());
        
        if (limiter == null) {
            log.error("Unknown algorithm type: {} for rule: {}", rule.algorithm(), rule.id());
            throw new IllegalArgumentException("Unknown algorithm: " + rule.algorithm());
        }
        
        log.debug("Using {} algorithm for key: {}, rule: {}", 
                rule.algorithm(), key, rule.id());
        
        // Delegate to the selected algorithm
        return limiter.tryAcquire(key, rule);
    }
}
