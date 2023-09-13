package com.sideproject.sns.fixture;

import com.sideproject.sns.model.entity.UserEntity;


public class UserEntityFixture {

    public static UserEntity get(Long userId, String username, String password) {
        return UserEntity.builder()
                .id(userId)
                .username(username)
                .password(password)
                .build();
    }
}
