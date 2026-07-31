package com.example.lotusplus.point.query.projection;

import com.example.lotusplus.point.enums.PointType;

import java.time.Instant;
import java.util.UUID;

public interface PointHistoryProjection {

    UUID getId();

    Integer getPoint();

    String getDescription();

    PointType getType();

    Instant getCreatedAt();
}