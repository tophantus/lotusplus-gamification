package com.example.lotusplus.point.command.dto;

import com.example.lotusplus.point.enums.PointType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AwardPointCommand {

    private UUID userId;

    private Integer point;

    private PointType type;

    private String referenceType;
    
    private UUID referenceId;

    private String description;

}
