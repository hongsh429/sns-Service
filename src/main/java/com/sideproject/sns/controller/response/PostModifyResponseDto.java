package com.sideproject.sns.controller.response;

import com.sideproject.sns.model.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostModifyResponseDto {

    private Long id;
    private String title;
    private String content;
    private UserResponseDto user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostModifyResponseDto fromPost(Post post) {
        return PostModifyResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(UserResponseDto.fromUser(post.getUser()))
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }
}
