package com.example.lotusplus.point.command.handler;

import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.point.command.dto.AwardPointCommand;
import com.example.lotusplus.point.command.repository.PointCommandRepository;
import com.example.lotusplus.point.entity.PointHistory;
import com.example.lotusplus.user.command.repository.UserCommandRepository;
import com.example.lotusplus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AwardPointHandler {

    private final UserCommandRepository userRepository;

    private final PointCommandRepository pointCommandRepository;

    @Transactional
    public void handle(AwardPointCommand command) {

        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.setLotusPoint(user.getLotusPoint() + command.getPoint());

        PointHistory history = PointHistory.builder()
                .user(user)
                .point(command.getPoint())
                .type(command.getType())
                .description(command.getDescription())
                .build();

        pointCommandRepository.save(history);
    }

}
