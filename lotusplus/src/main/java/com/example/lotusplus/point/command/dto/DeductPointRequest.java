package com.example.lotusplus.point.command.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeductPointRequest {

    @NotNull
    private UUID userId;

    @NotNull
    @Min(1)
    private Integer point;

    private String description;

}
