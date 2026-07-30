package com.example.lotusplus.point.command.handler;

import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.point.command.dto.DeductPointRequest;
import com.example.lotusplus.point.command.repository.PointCommandRepository;
import com.example.lotusplus.point.entity.PointHistory;
import com.example.lotusplus.point.enums.PointType;
import com.example.lotusplus.user.command.repository.UserCommandRepository;
import com.example.lotusplus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeductPointHandler {

    private final UserCommandRepository userRepository;
    private final PointCommandRepository pointCommandRepository;

    @Transactional
    public void handle(DeductPointRequest request) {

        if (request.getPoint() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_POINT);
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getLotusPoint() < request.getPoint()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_POINT);
        }

        user.setLotusPoint(
                user.getLotusPoint() - request.getPoint()
        );

        PointHistory history = PointHistory.builder()
                .user(user)
                .point(request.getPoint())
                .type(PointType.DEDUCTION)
                .description(request.getDescription())
                .build();

        pointCommandRepository.save(history);
    }
}
