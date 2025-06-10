package com.sellon.payment.service.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class InMemoryLockProvider implements LockProvider {
    private final Map<String, Object> paymentLocks = new ConcurrentHashMap<>();

    @Override
    public <T> T lock(String key, long timeoutMillis, Supplier<T> criticalSection) {
        // 결제 ID에 해당하는 락 객체 가져오기 (없으면 생성)
        Object lock = paymentLocks.computeIfAbsent(key, k -> new Object());

        synchronized (key) {
            return criticalSection.get();
        }
    }

}
