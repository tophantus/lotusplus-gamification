package com.example.lotusplus.checkin.controller;

import com.example.lotusplus.checkin.command.dto.CheckInResponse;
import com.example.lotusplus.checkin.command.handler.CheckInCommandHandler;
import com.example.lotusplus.checkin.query.dto.CheckInStatusResponse;
import com.example.lotusplus.checkin.query.handler.GetCheckInStatusHandler;
import com.example.lotusplus.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/checkins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInCommandHandler checkInHandler;

    private final GetCheckInStatusHandler statusHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<CheckInResponse>> checkIn(
            @RequestParam UUID userId
    ) {


        CheckInResponse response =
                checkInHandler.handle(userId);


        return ResponseEntity.ok(
                ApiResponse.success(response, "Success")
        );
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<CheckInStatusResponse>> getStatus(
            @RequestParam UUID userId
    ) {


        CheckInStatusResponse response =
                statusHandler.handle(userId);


        return ResponseEntity.ok(
                ApiResponse.success(response, "Success")
        );
    }

}