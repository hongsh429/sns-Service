package com.sideproject.sns.controller.response;


import com.sideproject.sns.model.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostMyFeedResponseDto {

    private Long id;
    private String title;
    private String content;
    private UserResponseDto user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostMyFeedResponseDto fromPost(Post post) {
        return PostMyFeedResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(UserResponseDto.fromUser(post.getUser()))
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }
}
