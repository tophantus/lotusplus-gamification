package com.example.lotusplus.user.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserProfileResponse {

    private UUID id;

    private String username;

    private String avatar;

    private Long lotusPoint;

}
