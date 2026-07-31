package com.example.lotusplus.checkin.query.handler;

import com.example.lotusplus.checkin.config.CheckInProperties;
import com.example.lotusplus.checkin.enums.CheckInStatus;
import com.example.lotusplus.checkin.query.dto.CheckInDayStatusResponse;
import com.example.lotusplus.checkin.query.dto.CheckInMonthSnapshot;
import com.example.lotusplus.checkin.query.dto.CheckInStatusResponse;
import com.example.lotusplus.checkin.query.repository.CheckInQueryRepository;
import com.example.lotusplus.user.query.handler.ValidateUserHandler;
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

    private final ValidateUserHandler validateUserHandler;
    private final CheckInQueryRepository checkInRepository;
    private final Clock clock;
    private final CheckInProperties properties;

    private final GetCheckInMonthSnapshotHandler snapshotHandler;

    public CheckInStatusResponse handle(UUID userId) {

        validateUserHandler.handle(userId);

        LocalDate today = LocalDate.now(clock);

        boolean checkedInToday =
                checkInRepository.existsByUserIdAndCheckinDate(
                        userId,
                        today
                );

        CheckInMonthSnapshot snapshot =
                snapshotHandler.handle(userId);

        int currentDay =
                checkedInToday
                        ? (int) Math.min(snapshot.getCheckedCount(), 7)
                        : (int) Math.min(snapshot.getCheckedCount() + 1, 7);

        List<CheckInDayStatusResponse> responses =
                snapshot.getDays()
                        .stream()
                        .map(day -> {

                            if (!checkedInToday
                                    && day.getDay() == currentDay
                                    && currentDay <= 7) {

                                return CheckInDayStatusResponse.builder()
                                        .day(day.getDay())
                                        .reward(day.getReward())
                                        .status(CheckInStatus.AVAILABLE)
                                        .build();
                            }

                            return day;
                        })
                        .toList();


        return CheckInStatusResponse.builder()
                .checkedInToday(checkedInToday)
                .canCheckIn(canCheckInNow() && !checkedInToday)
                .currentDay(currentDay)
                .days(responses)
                .build();
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