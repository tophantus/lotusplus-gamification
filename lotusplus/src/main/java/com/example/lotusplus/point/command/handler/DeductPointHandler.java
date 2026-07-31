package com.example.lotusplus.point.command.handler;

import com.example.lotusplus.common.cache.CacheNames;
import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.point.command.dto.DeductPointRequest;
import com.example.lotusplus.point.command.repository.PointCommandRepository;
import com.example.lotusplus.point.entity.PointHistory;
import com.example.lotusplus.point.enums.PointType;
import com.example.lotusplus.user.command.dto.UpdateUserPointCommand;
import com.example.lotusplus.user.command.handler.UpdateUserPointCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeductPointHandler {

    private final UpdateUserPointCommandHandler updateUserPointHandler;
    private final PointCommandRepository pointCommandRepository;

    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.USER_PROFILE,
            key = "#command.userId"
    )
    public void handle(DeductPointRequest command) {

        if (command.getPoint() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_POINT);
        }

        Long newBalance = updateUserPointHandler.handle(
                UpdateUserPointCommand.builder()
                        .userId(command.getUserId())
                        .amount(-command.getPoint())
                        .build()
        );

        PointHistory history = PointHistory.builder()
                .userId(command.getUserId())
                .point(command.getPoint())
                .balanceAfter(newBalance)
                .type(PointType.DEDUCTION)
                .description(command.getDescription())
                .build();

        pointCommandRepository.save(history);
    }
}
