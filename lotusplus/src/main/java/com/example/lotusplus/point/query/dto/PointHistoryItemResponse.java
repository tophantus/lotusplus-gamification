package com.example.lotusplus.point.query.dto;

import com.example.lotusplus.point.enums.PointType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class PointHistoryItemResponse {

    private UUID id;

    private Integer point;

    private Long balanceAfter;

    private PointType type;

    private String description;

    private Instant createdAt;

}
