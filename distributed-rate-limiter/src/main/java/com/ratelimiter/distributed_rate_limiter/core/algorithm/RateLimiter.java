package com.ratelimiter.distributed_rate_limiter.core.algorithm;

import com.ratelimiter.distributed_rate_limiter.core.algorithm.enums.RateLimiterType;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitResult;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitRule;

public interface RateLimiter {
    // I need to write the class definition for the fixedwindow here 
    // This method will be used to try to acquire the rate limit for the given key and rule     
    // It will return the result of the rate limit
    RateLimitResult tryAcquire(String key, RateLimitRule rule);
    // This method will be used to get the type of the rate limiter
    // It will return the type of the rate limiter
    RateLimiterType getType();
}
