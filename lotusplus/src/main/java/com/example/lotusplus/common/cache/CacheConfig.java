package com.example.lotusplus.common.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory factory
    ) {

        RedisSerializer<Object> serializer =
                RedisSerializer.json();

        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(RedisSerializer.string())
                        )
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(serializer)
                        )
                        .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> configs = new HashMap<>();

        configs.put(
                CacheNames.USER_PROFILE,
                defaultConfig.entryTtl(CacheTtl.USER_PROFILE.getDuration())
        );

        configs.put(
                CacheNames.REWARD_CONFIG,
                defaultConfig.entryTtl(CacheTtl.REWARD_CONFIG.getDuration())
        );

        configs.put(
                CacheNames.CHECKIN_MONTH,
                defaultConfig.entryTtl(CacheTtl.CHECKIN_MONTH.getDuration())
        );

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(configs)
                .build();
    }

}