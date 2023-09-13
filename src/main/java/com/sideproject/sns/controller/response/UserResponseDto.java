package com.sideproject.sns.controller.response;


import com.sideproject.sns.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String username;

    public static UserResponseDto fromUser(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

}
