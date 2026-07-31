package com.example.lotusplus.reward.service;

import com.example.lotusplus.common.cache.CacheNames;
import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.reward.entity.RewardConfig;
import com.example.lotusplus.reward.query.repository.RewardQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardQueryRepository rewardRepository;

    @Transactional(readOnly = true)
    public Integer getRewardByDay(Integer day) {

        return getRewardMap()
                .getOrDefault(
                        day,
                        throwRewardConfigNotFound()
                );
    }

    @Transactional(readOnly = true)
    public Map<Integer, Integer> getRewardMap() {

        return getRewardConfigs()
                .stream()
                .collect(Collectors.toMap(
                        RewardConfig::getDayNo,
                        RewardConfig::getReward
                ));
    }

    @Cacheable(
            cacheNames = CacheNames.REWARD_CONFIG,
            sync = true
    )
    @Transactional(readOnly = true)
    protected List<RewardConfig> getRewardConfigs() {

        return rewardRepository.findAll();
    }

    private Integer throwRewardConfigNotFound() {

        throw new BusinessException(
                ErrorCode.REWARD_CONFIG_NOT_FOUND
        );
    }
}