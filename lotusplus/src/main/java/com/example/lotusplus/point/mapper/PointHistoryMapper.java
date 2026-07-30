package com.example.lotusplus.point.mapper;

import com.example.lotusplus.point.entity.PointHistory;
import com.example.lotusplus.point.query.dto.PointHistoryItemResponse;

public final class PointHistoryMapper {

    private PointHistoryMapper() {
    }

    public static PointHistoryItemResponse toResponse(PointHistory history) {

        return PointHistoryItemResponse.builder()
                .id(history.getId())
                .point(history.getPoint())
                .type(history.getType())
                .description(history.getDescription())
                .createdAt(history.getCreatedAt())
                .build();
    }

}
