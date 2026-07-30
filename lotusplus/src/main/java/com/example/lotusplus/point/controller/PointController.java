package com.example.lotusplus.point.controller;

import com.example.lotusplus.common.response.ApiResponse;
import com.example.lotusplus.point.command.dto.DeductPointRequest;
import com.example.lotusplus.point.command.handler.DeductPointHandler;
import com.example.lotusplus.point.query.dto.PointHistoryResponse;
import com.example.lotusplus.point.query.handler.GetPointHistoryHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

    private final DeductPointHandler deductPointHandler;

    private final GetPointHistoryHandler getPointHistoryHandler;

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getPointHistory(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PointHistoryResponse response =
                getPointHistoryHandler.handle(userId, page, size);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Success")
        );
    }

    @PostMapping("/deduct")
    public ResponseEntity<ApiResponse<UUID>> deductPoint(
            @Valid @RequestBody DeductPointRequest request
    ) {

        deductPointHandler.handle(request);

        return ResponseEntity.ok(
                ApiResponse.success(request.getUserId(), "Success")
        );
    }

}