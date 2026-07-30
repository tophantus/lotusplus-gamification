package com.example.lotusplus.user.controller;

import com.example.lotusplus.common.response.ApiResponse;
import com.example.lotusplus.user.command.dto.CreateUserRequest;
import com.example.lotusplus.user.command.handler.CreateUserCommandHandler;
import com.example.lotusplus.user.query.dto.UserProfileResponse;
import com.example.lotusplus.user.query.handler.GetUserProfileQueryHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserCommandHandler createUserHandler;
    private final GetUserProfileQueryHandler getUserProfileHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<UUID>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {

        UUID userId = createUserHandler.handle(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(userId, "Success"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @PathVariable UUID userId
    ) {

        UserProfileResponse response =
                getUserProfileHandler.handle(userId);

        return ResponseEntity.ok(ApiResponse.success(response, "Success"));
    }

}