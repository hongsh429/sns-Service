package com.sideproject.sns.fixture;

import com.sideproject.sns.model.entity.PostEntity;
import com.sideproject.sns.model.entity.UserEntity;


public class PostEntityFixture {

    public static PostEntity get(Long userId, String username, Long postId) {
        UserEntity userEntity = getUser(userId, username);

        return PostEntity.builder()
                .id(postId)
                .user(userEntity)
                .build();
    }

    private static UserEntity getUser(Long userId, String username) {
        return UserEntity.builder()
                .id(userId)
                .username(username)
                .build();
    }


}
