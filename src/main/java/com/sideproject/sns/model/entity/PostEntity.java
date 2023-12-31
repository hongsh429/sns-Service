package com.sideproject.sns.model.entity;


import com.sideproject.sns.model.BaseAuditing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity extends BaseAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static PostEntity of(String title, String content, UserEntity userEntity) {
        return PostEntity.builder()
                .title(title)
                .content(content)
                .user(userEntity)
                .build();
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
