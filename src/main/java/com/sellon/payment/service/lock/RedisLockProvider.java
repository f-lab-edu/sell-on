package com.sellon.payment.service.lock;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class RedisLockProvider implements LockProvider {
    private final RedisTemplate redisTemplate;

    @Override
    public <T> T lock(String key, long timeoutMillis, Supplier<T> criticalSection) {
        String lockValue = UUID.randomUUID().toString();
        boolean acquired = acquireLock(key, lockValue, timeoutMillis);

        if (!acquired) {
            throw new IllegalStateException("락 획득에 실패했습니다: " + key);
        }

        try {
            return criticalSection.get();
        } finally {
            releaseLock(key, lockValue);
        }
    }

    private boolean acquireLock(String lock, String lockValue, long timeoutMillis) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(lock, lockValue, timeoutMillis, TimeUnit.MILLISECONDS)
        );
    }

    private void releaseLock(String lock, String lockValue) {
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "   return redis.call('del', KEYS[1] " +
                        "else " +
                        "   return 0 " +
                        "end";

        redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(lock),
                lockValue
        );
    }
}
