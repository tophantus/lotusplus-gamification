package com.example.lotusplus.user.command.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateUserPointCommand {

    private UUID userId;

    /**
     * +100: cộng điểm
     * -100: trừ điểm
     */
    private Integer amount;
}