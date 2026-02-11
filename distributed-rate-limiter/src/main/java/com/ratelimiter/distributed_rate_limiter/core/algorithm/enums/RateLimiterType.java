package com.ratelimiter.distributed_rate_limiter.core.algorithm.enums;

public enum RateLimiterType {
    FIXED_WINDOW,
    SLIDING_WINDOW_LOG,
    SLIDING_WINDOW_COUNTER,
    TOKEN_BUCKET
}
