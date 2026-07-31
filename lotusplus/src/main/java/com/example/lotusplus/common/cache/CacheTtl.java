package com.example.lotusplus.common.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum CacheTtl {

    USER_PROFILE(Duration.ofMinutes(10)),
    REWARD_CONFIG(Duration.ofHours(24)),
    CHECKIN_MONTH(Duration.ofMinutes(15));

    private final Duration duration;
}
