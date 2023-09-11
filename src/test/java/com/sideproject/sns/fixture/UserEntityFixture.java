package com.sideproject.sns.fixture;

import com.sideproject.sns.model.entity.UserEntity;


public class UserEntityFixture {

    public static UserEntity get(String username, String password) {
        return UserEntity.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();
    }
}
