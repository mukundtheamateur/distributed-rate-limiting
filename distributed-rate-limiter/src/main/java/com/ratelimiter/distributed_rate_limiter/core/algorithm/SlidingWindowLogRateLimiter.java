package com.ratelimiter.distributed_rate_limiter.core.algorithm;

import com.ratelimiter.distributed_rate_limiter.core.algorithm.enums.RateLimiterType;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitResult;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitRule;

public class SlidingWindowLogRateLimiter implements RateLimiter{
    @Override
    public RateLimitResult tryAcquire(String key, RateLimitRule rule) {
        return null;
    }

    @Override
    public RateLimiterType getType() {
        return RateLimiterType.SLIDING_WINDOW_LOG;
    }
}
