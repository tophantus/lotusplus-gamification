package com.example.lotusplus.reward.query.repository;

import com.example.lotusplus.reward.entity.RewardConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardQueryRepository
        extends JpaRepository<RewardConfig, Integer> {
}