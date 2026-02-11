package com.ratelimiter.distributed_rate_limiter.core.algorithm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ratelimiter.distributed_rate_limiter.core.algorithm.enums.RateLimiterType;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitResult;
import com.ratelimiter.distributed_rate_limiter.core.model.RateLimitRule;

import lombok.extern.slf4j.Slf4j;

// I need to write an algo here for the fixed window rate limiter
// this fixed window will have the timestamp as the window and we will be using ConCurrentHashMap<String, AtomicInteger>
/**
 * This algo will divide the time into fixed windows, 
 * each window will have a counter starting at 0,
 * each time a request comes in, the counter will be incremented,
 * if the counter exceeds the limit, the request will be denied,
 * otherwise the request will be allowed.
 * 
 * The key will be the client id and the window index will be the current time in milliseconds divided by the window size in milliseconds
 * The window size will be the window size in milliseconds
 * The window size will be the window size in milliseconds
 */
@Slf4j
public class FixedWindowRateLimiter implements RateLimiter {

    private final ConcurrentHashMap<String, AtomicInteger> windowCounts = new ConcurrentHashMap<>();
    @Override
    public RateLimitResult tryAcquire(String key, RateLimitRule rule) {
        try {
            // why divide by rule.window().toMillis()?
            // because we want to get the current window index
            // the window index is the current time in milliseconds divided by the window size in milliseconds
            // the window size is the window size in milliseconds
            long windowSizeMillis = rule.window().toMillis();
            long currentTimeMillis = System.currentTimeMillis();
            long currentWindowIndex = currentTimeMillis/ windowSizeMillis;
            
            String windowKey = key + ":" + currentWindowIndex;

            log.debug("Rate limit check - key: {}, rule: {}, currentWindowIndex: {}, windowKey: {}", 
                        key, rule.id(), currentWindowIndex, windowKey);

            // now I need to create counter for this window, then increment atomically
            int currentCount = windowCounts.computeIfAbsent(windowKey, k -> {
                log.debug("Creating new window count for key: {}", k);
                return new AtomicInteger(0);
            }).incrementAndGet();

            // we need to calculate when this window resets(start of next window)
            long windowStartMillis = currentWindowIndex * windowSizeMillis;
            long windowResetMillis = windowStartMillis + windowSizeMillis;
            long resetEpochSeconds = windowResetMillis / 1000;

            int limit = rule.maxRequests();

            if (currentCount <= limit) {
                // allowed
                int remaining = limit - currentCount;
                log.info("Rate limit check - key: {}, rule: {}, currentWindowIndex: {}, windowKey: {}, currentCount: {}, remaining: {}, resetEpochSeconds: {}", 
                        key, rule.id(), currentWindowIndex, windowKey, currentCount, remaining, resetEpochSeconds);
                return RateLimitResult.allowed(remaining, limit, resetEpochSeconds);
            } else {
                // denied
                long retryAfterMs = windowResetMillis - currentTimeMillis;
                log.info("Rate limit check - key: {}, rule: {}, currentWindowIndex: {}, windowKey: {}, currentCount: {}, limit: {}, retryAfterMs: {}", 
                        key, rule.id(), currentWindowIndex, windowKey, currentCount, limit, retryAfterMs);
                return RateLimitResult.denied(limit, retryAfterMs);
            }
        } catch (Exception e) {
            log.error("Error in rate limit check - key: {}, rule: {}, error: {}", key, rule.id(), e.getMessage());
            throw new RuntimeException("Rate limit check failed",e);
        }
    }

    @Override
    public RateLimiterType getType() {
        return RateLimiterType.FIXED_WINDOW;
    }
}
