package com.example.lotusplus.user.mapper;

import com.example.lotusplus.user.entity.User;
import com.example.lotusplus.user.query.dto.UserProfileResponse;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserProfileResponse toProfile(User user) {

        if (user == null) {
            return null;
        }

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .lotusPoint(user.getLotusPoint())
                .build();
    }

}
