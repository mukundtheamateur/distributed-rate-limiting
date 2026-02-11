package com.ratelimiter.distributed_rate_limiter.core.algorithm;

import com.ratelimiter.distributed_rate_limiter.core.algorithm.enums.RateLimiterType;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitResult;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitRule;

// I need to write an algo here for the fixed window rate limiter
// this fixed window will have the timestamp as the window and we will be using ConCurrentHashMap<String, AtomicInteger>

public class FixedWindowRateLimiter implements RateLimiter {
    @Override
    public RateLimitResult tryAcquire(String key, RateLimitRule rule) {
        return null;
    }

    @Override
    public RateLimiterType getType() {
        return RateLimiterType.FIXED_WINDOW;
    }
}
