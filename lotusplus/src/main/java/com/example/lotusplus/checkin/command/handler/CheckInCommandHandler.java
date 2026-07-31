package com.example.lotusplus.checkin.command.handler;

import com.example.lotusplus.checkin.command.dto.CheckInResponse;
import com.example.lotusplus.checkin.command.repository.CheckInCommandRepository;
import com.example.lotusplus.checkin.config.CheckInProperties;
import com.example.lotusplus.checkin.entity.CheckIn;
import com.example.lotusplus.checkin.query.repository.CheckInQueryRepository;
import com.example.lotusplus.common.cache.CacheNames;
import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.point.command.dto.AwardPointCommand;
import com.example.lotusplus.point.command.handler.AwardPointHandler;
import com.example.lotusplus.point.enums.PointType;
import com.example.lotusplus.reward.service.RewardService;
import com.example.lotusplus.user.command.repository.UserCommandRepository;
import com.example.lotusplus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckInCommandHandler {

    private final CheckInProperties checkInProperties;

    private final UserCommandRepository userRepository;

    private final CheckInCommandRepository checkInRepository;

    private final CheckInQueryRepository checkInQueryRepository;

    private final Clock clock;

    private final RewardService rewardService;

    private final AwardPointHandler awardPointHandler;

    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CHECKIN_MONTH,
            key = "#userId"
    )
    public CheckInResponse handle(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now(clock);

        if (checkInQueryRepository.existsByUserIdAndCheckinDate(userId, today)) {
            throw new BusinessException(ErrorCode.ALREADY_CHECKED_IN);
        }

        validateCheckInTime();

        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        long checkedDays = checkInQueryRepository
                .countByUserIdAndCheckinDateBetween(
                        userId,
                        firstDay,
                        lastDay
                );

        if (checkedDays >= 7) {
            throw new BusinessException(ErrorCode.MAX_CHECKIN_REACHED);
        }

        int currentDay = (int) checkedDays + 1;

        Integer reward = rewardService.getRewardByDay(currentDay);

        CheckIn checkIn = CheckIn.builder()
                .userId(userId)
                .checkinDate(today)
                .reward(reward)
                .build();

        checkInRepository.save(checkIn);

        awardPointHandler.handle(
                AwardPointCommand.builder()
                        .userId(userId)
                        .point(reward)
                        .type(PointType.CHECK_IN)
                        .referenceId(checkIn.getId())
                        .referenceType("CHECK_IN")
                        .description("Daily check-in")
                        .build()
        );

        return CheckInResponse.builder()
                .day(currentDay)
                .reward(reward)
                .totalPoint(user.getLotusPoint().intValue())
                .build();
    }

    private void validateCheckInTime() {

        LocalTime now = LocalTime.now(clock);

        boolean morning =
                !now.isBefore(checkInProperties.getMorning().getStart())
                        && now.isBefore(checkInProperties.getMorning().getEnd());

        boolean evening =
                !now.isBefore(checkInProperties.getEvening().getStart())
                        && now.isBefore(checkInProperties.getEvening().getEnd());

        if (!(morning || evening)) {
            throw new BusinessException(ErrorCode.CHECKIN_NOT_AVAILABLE);
        }
    }

}