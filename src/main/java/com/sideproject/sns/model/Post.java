package com.sideproject.sns.model;


import com.sideproject.sns.model.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Post {

    private Long id;
    private String title;
    private String content;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static Post fromEntity(PostEntity postEntity) {
        return Post.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .user(User.fromEntity(postEntity.getUser()))
                .createdAt(postEntity.getCreatedAt())
                .modifiedAt(postEntity.getModifiedAt())
                .build();
    }
}
