package com.example.lotusplus.point.mapper;

import com.example.lotusplus.point.query.dto.PointHistoryItemResponse;
import com.example.lotusplus.point.query.projection.PointHistoryProjection;

public final class PointHistoryMapper {

    private PointHistoryMapper() {
    }

    public static PointHistoryItemResponse toResponse(
            PointHistoryProjection projection
    ) {

        return PointHistoryItemResponse.builder()
                .id(projection.getId())
                .point(projection.getPoint())
                .description(projection.getDescription())
                .type(projection.getType())
                .createdAt(projection.getCreatedAt())
                .build();
    }

}
