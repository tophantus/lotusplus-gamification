package com.example.lotusplus.common.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String key();

    String prefix() default "lock:";

    long waitTime() default 3;

    TimeUnit unit() default TimeUnit.SECONDS;
}