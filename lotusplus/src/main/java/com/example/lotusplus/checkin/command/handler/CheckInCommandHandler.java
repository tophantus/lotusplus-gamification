package com.example.lotusplus.checkin.command.handler;

import com.example.lotusplus.checkin.command.dto.CheckInResponse;
import com.example.lotusplus.checkin.command.repository.CheckInCommandRepository;
import com.example.lotusplus.checkin.config.CheckInProperties;
import com.example.lotusplus.checkin.entity.CheckIn;
import com.example.lotusplus.checkin.query.repository.CheckInQueryRepository;
import com.example.lotusplus.common.cache.CacheNames;
import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.common.lock.DistributedLock;
import com.example.lotusplus.point.command.dto.AwardPointCommand;
import com.example.lotusplus.point.command.handler.AwardPointHandler;
import com.example.lotusplus.point.enums.PointType;
import com.example.lotusplus.reward.service.RewardService;
import com.example.lotusplus.user.query.handler.ValidateUserHandler;
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

    private static final int MAX_CHECKIN_DAYS = 7;

    private final CheckInProperties checkInProperties;
    private final ValidateUserHandler validateUserHandler;
    private final CheckInCommandRepository checkInRepository;
    private final CheckInQueryRepository checkInQueryRepository;
    private final Clock clock;
    private final RewardService rewardService;
    private final AwardPointHandler awardPointHandler;

    @DistributedLock(
            prefix = "lock:checkin:user:",
            key = "#userId"
    )
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CHECKIN_MONTH,
            key = "#userId"
    )
    public CheckInResponse handle(UUID userId) {

        validateUserHandler.handle(userId);

        LocalDate today = LocalDate.now(clock);

        validateAlreadyChecked(userId, today);
        validateCheckInTime();

        long checkedDays = getCheckedDays(userId, today);
        validateCheckInLimit(checkedDays);


        int currentDay = (int) checkedDays + 1;
        Integer reward = rewardService.getRewardByDay(currentDay);

        CheckIn checkIn = saveCheckIn(userId, today, reward);

        Long totalPoint = awardPointHandler.handle(
                buildAwardPointCommand(
                        userId,
                        reward,
                        checkIn.getId()
                )
        );

        return CheckInResponse.builder()
                .day(currentDay)
                .reward(reward)
                .totalPoint(totalPoint)
                .build();
    }

    private void validateAlreadyChecked(
            UUID userId,
            LocalDate today
    ) {

        if (checkInQueryRepository.existsByUserIdAndCheckinDate(userId, today)) {
            throw new BusinessException(ErrorCode.ALREADY_CHECKED_IN);
        }
    }


    private long getCheckedDays(
            UUID userId,
            LocalDate today
    ) {

        return checkInQueryRepository.countByUserIdAndCheckinDateBetween(
                userId,
                today.withDayOfMonth(1),
                today.withDayOfMonth(today.lengthOfMonth())
        );
    }

    private void validateCheckInLimit(long checkedDays) {

        if (checkedDays >= MAX_CHECKIN_DAYS) {
            throw new BusinessException(ErrorCode.MAX_CHECKIN_REACHED);
        }
    }

    private CheckIn saveCheckIn(
            UUID userId,
            LocalDate today,
            Integer reward
    ) {

        CheckIn checkIn = CheckIn.builder()
                .userId(userId)
                .checkinDate(today)
                .reward(reward)
                .build();

        return checkInRepository.save(checkIn);
    }

    private AwardPointCommand buildAwardPointCommand(
            UUID userId,
            Integer reward,
            UUID checkInId
    ) {

        return AwardPointCommand.builder()
                .userId(userId)
                .point(reward)
                .type(PointType.CHECK_IN)
                .referenceType("CHECK_IN")
                .referenceId(checkInId)
                .description("Daily check-in")
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