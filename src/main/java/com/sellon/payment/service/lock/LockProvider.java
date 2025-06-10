package com.sellon.payment.service.lock;

import java.util.function.Supplier;

public interface LockProvider {
    <T> T lock(String key, long timeoutMillis, Supplier<T> criticalSection);
}
