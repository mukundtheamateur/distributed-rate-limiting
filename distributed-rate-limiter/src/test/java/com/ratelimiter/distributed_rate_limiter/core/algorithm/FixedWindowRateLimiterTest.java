package com.ratelimiter.distributed_rate_limiter.core.algorithm;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ratelimiter.distributed_rate_limiter.core.algorithm.enums.RateLimiterType;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitResult;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitRule;

public class FixedWindowRateLimiterTest {
    @Test
    void shouldAllowRequestsWithinLimit() {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter();
        RateLimitRule rule = new RateLimitRule(
            "test-rule",
            5,  // limit: 5 requests
            Duration.ofSeconds(60),  // window: 60 seconds
            RateLimiterType.FIXED_WINDOW,
            "test"
        );
        
        String key = "client-1";
        
        // First 5 requests should be allowed
        for (int i = 0; i < 5; i++) {
            RateLimitResult result = limiter.tryAcquire(key, rule);
            Assertions.assertTrue(result.allowed());
        }
        
        // 6th request should be denied
        RateLimitResult result = limiter.tryAcquire(key, rule);
        Assertions.assertFalse(result.allowed(), "Expected denied result");
    }
}
