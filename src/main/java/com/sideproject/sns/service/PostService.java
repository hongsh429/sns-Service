package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.exception.ErrorCode;
import com.sideproject.sns.model.Post;
import com.sideproject.sns.model.entity.LikeEntity;
import com.sideproject.sns.model.entity.PostEntity;
import com.sideproject.sns.model.entity.UserEntity;
import com.sideproject.sns.repository.LikeEntityRepository;
import com.sideproject.sns.repository.PostEntityRepository;
import com.sideproject.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    @Transactional
    public void createPost(String title, String content, String username) {
        // user find
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NO_DATA, String.format("%s: 유저가 존재하지 않습니다", username))
        );

        // post save
        postEntityRepository.save(PostEntity.of(title, content, userEntity));
    }

    @Transactional
    public Post modifyPost(String title, String content, String username, Long postId) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NO_DATA, String.format("%s: 유저가 존재하지 않습니다", username))
        );

        // if post exists
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorCode.NO_DATA, "해당 포스트는 존재하지 않습니다.")
        );

        // if user qualifies for modifying post
        // Spring Data JPA에선, 두 개의 객체가 동일한지 아닌지 비교가 가능하다.
        if (userEntity != postEntity.getUser()) {
            throw new CustomException(ErrorCode.NO_PERMISSION, String.format("%s: 해당 글(%s)에 수정 권한이 없습니다", username, postId));
        }

        // post modify
        postEntity.modify(title, content);
        postEntityRepository.saveAndFlush(postEntity);

        return Post.fromEntity(postEntity);
    }

    @Transactional
    public void deletePost(Long postId, String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NO_DATA, String.format("%s: 유저가 존재하지 않습니다", username))
        );

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorCode.NO_DATA, "해당 포스트는 존재하지 않습니다.")
        );

        if (userEntity != postEntity.getUser()) {
            throw new CustomException(ErrorCode.NO_PERMISSION, String.format("%s: 해당 글(%s)에 삭제 권한이 없습니다", username, postId));
        }

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> listPost(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> listMyFeed(Pageable pageable, String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NO_DATA, String.format("%s: 유저가 존재하지 않습니다", username))
        );

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void likePost(Long postId, String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NO_DATA, String.format("%s: 유저가 존재하지 않습니다", username))
        );

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorCode.NO_DATA, "해당 포스트는 존재하지 않습니다.")
        );

        // like 존재 여부 확인
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(entity -> {
            throw new CustomException(ErrorCode.DUPLICATED, "이미 게시글좋아요 상태입니다");
        });

        // builder 패턴이 필요할까?
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    public Long countLikeAtPost(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorCode.NO_DATA, "해당 포스트는 존재하지 않습니다.")
        );

        return likeEntityRepository.countAllByPost(postEntity);
    }
}
