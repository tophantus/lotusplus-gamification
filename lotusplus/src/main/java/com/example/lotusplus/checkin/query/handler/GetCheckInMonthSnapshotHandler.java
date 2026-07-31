package com.example.lotusplus.checkin.query.handler;

import com.example.lotusplus.checkin.enums.CheckInStatus;
import com.example.lotusplus.checkin.query.dto.CheckInDayStatusResponse;
import com.example.lotusplus.checkin.query.dto.CheckInMonthSnapshot;
import com.example.lotusplus.checkin.query.repository.CheckInQueryRepository;
import com.example.lotusplus.common.cache.CacheNames;
import com.example.lotusplus.reward.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCheckInMonthSnapshotHandler {

    private final CheckInQueryRepository checkInRepository;
    private final RewardService rewardService;
    private final Clock clock;

    @Cacheable(
            cacheNames = CacheNames.CHECKIN_MONTH,
            key = "#userId"
    )
    public CheckInMonthSnapshot handle(UUID userId) {

        LocalDate today = LocalDate.now(clock);

        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        long checkedCount =
                checkInRepository.countByUserIdAndCheckinDateBetween(
                        userId,
                        firstDay,
                        lastDay
                );

        Map<Integer, Integer> rewardMap =
                rewardService.getRewardMap();

        List<CheckInDayStatusResponse> days =
                buildDays(
                        checkedCount,
                        rewardMap
                );

        return CheckInMonthSnapshot.builder()
                .checkedCount(checkedCount)
                .days(days)
                .build();
    }

    private List<CheckInDayStatusResponse> buildDays(
            long checkedCount,
            Map<Integer, Integer> rewardMap
    ) {

        List<CheckInDayStatusResponse> result = new ArrayList<>();

        for (int day = 1; day <= 7; day++) {

            CheckInStatus status =
                    day <= checkedCount
                            ? CheckInStatus.CHECKED
                            : CheckInStatus.LOCKED;

            result.add(
                    CheckInDayStatusResponse.builder()
                            .day(day)
                            .reward(rewardMap.get(day))
                            .status(status)
                            .build()
            );
        }

        return result;
    }
}