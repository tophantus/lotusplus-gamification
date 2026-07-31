package com.example.lotusplus.user.command.handler;

import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.user.command.dto.UpdateUserPointCommand;
import com.example.lotusplus.user.command.repository.UserCommandRepository;
import com.example.lotusplus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserPointCommandHandler {

    private final UserCommandRepository userRepository;

    @Transactional
    public Long handle(UpdateUserPointCommand command) {

        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.USER_NOT_FOUND));

        long newBalance =
                user.getLotusPoint() + command.getAmount();

        if (newBalance < 0) {
            throw new BusinessException(
                    ErrorCode.INSUFFICIENT_POINT
            );
        }

        user.setLotusPoint(newBalance);

        return newBalance;
    }
}