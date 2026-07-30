package com.example.lotusplus.user.query.handler;

import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import com.example.lotusplus.user.mapper.UserMapper;
import com.example.lotusplus.user.query.dto.UserProfileResponse;
import com.example.lotusplus.user.query.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserProfileQueryHandler {

    private final UserQueryRepository userQueryRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse handle(UUID id) {

        return userQueryRepository.findById(id)
                .map(UserMapper::toProfile)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

}
