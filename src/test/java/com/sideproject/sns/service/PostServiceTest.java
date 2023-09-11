package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.exception.ErrorCode;
import com.sideproject.sns.model.entity.PostEntity;
import com.sideproject.sns.model.entity.UserEntity;
import com.sideproject.sns.repository.PostEntityRepository;
import com.sideproject.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    PostEntityRepository postEntityRepository;

    @MockBean
    UserEntityRepository userEntityRepository;

    @Test
    void 포스트작성_성공() {
        String title = "title";
        String content = "content";
        String username = "username";

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        Assertions.assertDoesNotThrow(() -> postService.createPost(title, content, username));
    }

    @Test
    void 포스트작성_요청유저_존재_X() {
        String title = "title";
        String content = "content";
        String username = "username";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.createPost(title, content, username));

        Assertions.assertEquals(ErrorCode.NO_DATA, ex.getErrorCode());
        verify(postEntityRepository, never()).save(new PostEntity());
    }
}
