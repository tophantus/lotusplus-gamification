package com.example.lotusplus.reward.service;

import com.example.lotusplus.common.cache.CacheNames;
import com.example.lotusplus.reward.entity.RewardConfig;
import com.example.lotusplus.reward.query.repository.RewardQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardConfigCacheService {

    private final RewardQueryRepository rewardRepository;

    @Cacheable(
            cacheNames = CacheNames.REWARD_CONFIG,
            sync = true
    )
    @Transactional(readOnly = true)
    public List<RewardConfig> getRewardConfigs() {

        return rewardRepository.findAll();
    }
}