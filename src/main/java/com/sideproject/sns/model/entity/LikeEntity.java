package com.sideproject.sns.model.entity;


import com.sideproject.sns.model.BaseAuditing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class LikeEntity extends BaseAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public static LikeEntity of(UserEntity userEntity, PostEntity postEntity) {
        return LikeEntity.builder()
                .user(userEntity)
                .post(postEntity)
                .build();
    }
}
