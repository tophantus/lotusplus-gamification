package com.example.lotusplus.reward.service;

import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.reward.entity.RewardConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardConfigCacheService rewardConfigCacheService;

    @Transactional(readOnly = true)
    public Integer getRewardByDay(Integer day) {

        Integer reward = getRewardMap().get(day);

        if (reward == null) {
            throwRewardConfigNotFound();
        }

        return reward;
    }

    @Transactional(readOnly = true)
    public Map<Integer, Integer> getRewardMap() {

        return rewardConfigCacheService.getRewardConfigs()
                .stream()
                .collect(Collectors.toMap(
                        RewardConfig::getDayNo,
                        RewardConfig::getReward
                ));
    }

    private void throwRewardConfigNotFound() {

        throw new BusinessException(
                ErrorCode.REWARD_CONFIG_NOT_FOUND
        );
    }
}