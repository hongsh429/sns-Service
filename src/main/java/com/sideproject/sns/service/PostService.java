package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.exception.ErrorCode;
import com.sideproject.sns.model.entity.PostEntity;
import com.sideproject.sns.repository.PostEntityRepository;
import com.sideproject.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    @Transactional
    public void createPost(String title, String content, String username) {
        // user find
        userEntityRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NO_DATA, String.format("%s: 유저가 존재하지 않습니다", username))
        );

        // post save
        postEntityRepository.save(new PostEntity());
        // return
    }
}
