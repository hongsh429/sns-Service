package com.sideproject.sns.controller.response;

import com.sideproject.sns.model.User;
import com.sideproject.sns.model.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserJoinResponseDto {

    private Long id;
    private String username;
    private UserRole role;

    public static UserJoinResponseDto fromUser(User user) {
        return UserJoinResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
