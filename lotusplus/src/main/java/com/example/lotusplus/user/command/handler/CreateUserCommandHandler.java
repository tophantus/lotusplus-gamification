package com.example.lotusplus.user.command.handler;

import com.example.lotusplus.user.command.dto.CreateUserRequest;
import com.example.lotusplus.user.command.repository.UserCommandRepository;
import com.example.lotusplus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateUserCommandHandler {

    private final UserCommandRepository userCommandRepository;

    @Transactional
    public UUID handle(CreateUserRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .avatar(request.getAvatar())
                .lotusPoint(0L)
                .build();

        user = userCommandRepository.save(user);

        return user.getId();
    }

}
