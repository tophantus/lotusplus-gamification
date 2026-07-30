package com.example.lotusplus.checkin.query.handler;

import com.example.lotusplus.checkin.config.CheckInProperties;
import com.example.lotusplus.checkin.entity.CheckIn;
import com.example.lotusplus.checkin.enums.CheckInStatus;
import com.example.lotusplus.checkin.query.dto.CheckInDayStatusResponse;
import com.example.lotusplus.checkin.query.dto.CheckInStatusResponse;
import com.example.lotusplus.checkin.query.repository.CheckInQueryRepository;
import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.reward.service.RewardService;
import com.example.lotusplus.user.query.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCheckInStatusHandler {

    private final UserQueryRepository userRepository;
    private final CheckInQueryRepository checkInRepository;
    private final RewardService rewardService;
    private final Clock clock;
    private final CheckInProperties properties;

    public CheckInStatusResponse handle(UUID userId) {

        if (!userRepository.existsById(userId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        LocalDate today = LocalDate.now(clock);

        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        boolean checkedInToday =
                checkInRepository.existsByUserIdAndCheckinDate(
                        userId,
                        today
                );

        long checkedCount =
                checkInRepository.countByUserIdAndCheckinDateBetween(
                        userId,
                        firstDay,
                        lastDay
                );

        List<CheckIn> histories = true
                ? List.of()
                : checkInRepository.findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(
                        userId,
                        firstDay,
                        lastDay
                );

        Map<Integer, Integer> rewardMap =
                rewardService.getRewardMap();

        int currentDay = checkedInToday
                ? (int) Math.min(checkedCount, 7)
                : (int) Math.min(checkedCount + 1, 7);

        List<CheckInDayStatusResponse> responses =
                buildDayStatuses(
                        histories,
                        rewardMap,
                        checkedCount,
                        currentDay
                );



        return CheckInStatusResponse.builder()
                .checkedInToday(checkedInToday)
                .canCheckIn(canCheckInNow() && !checkedInToday)
                .currentDay(currentDay)
                .days(responses)
                .build();
    }

    private List<CheckInDayStatusResponse> buildDayStatuses(
            List<CheckIn> histories,
            Map<Integer, Integer> rewardMap,
            long checkedCount,
            int currentDay
    ) {

        List<CheckInDayStatusResponse> result = new ArrayList<>();

        for (int day = 1; day <= 7; day++) {

            CheckInStatus status;

            if (day <= checkedCount) {
                status = CheckInStatus.CHECKED;
            } else if (day == currentDay && checkedCount < 7) {
                status = CheckInStatus.AVAILABLE;
            } else {
                status = CheckInStatus.LOCKED;
            }

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

    private boolean canCheckInNow() {

        LocalTime now = LocalTime.now(clock);

        boolean morning =
                !now.isBefore(properties.getMorning().getStart())
                        && now.isBefore(properties.getMorning().getEnd());

        boolean evening =
                !now.isBefore(properties.getEvening().getStart())
                        && now.isBefore(properties.getEvening().getEnd());

        return morning || evening;
    }
}