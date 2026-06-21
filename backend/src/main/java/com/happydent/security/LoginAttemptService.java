package com.happydent.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MS = 15 * 60 * 1000L; // 15 minutes

    private final AtomicInteger failCount = new AtomicInteger(0);
    private volatile Instant lockedUntil = null;

    public boolean isLocked() {
        if (lockedUntil != null && Instant.now().isBefore(lockedUntil)) return true;
        if (lockedUntil != null) {
            failCount.set(0);
            lockedUntil = null;
        }
        return false;
    }

    public void recordFailure() {
        if (failCount.incrementAndGet() >= MAX_ATTEMPTS) {
            lockedUntil = Instant.now().plusMillis(LOCK_DURATION_MS);
        }
    }

    public void recordSuccess() {
        failCount.set(0);
        lockedUntil = null;
    }

    public long secondsUntilUnlock() {
        if (lockedUntil == null) return 0;
        return Math.max(0, lockedUntil.getEpochSecond() - Instant.now().getEpochSecond());
    }
}
