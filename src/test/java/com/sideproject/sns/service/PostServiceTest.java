package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.exception.ErrorCode;
import com.sideproject.sns.fixture.PostEntityFixture;
import com.sideproject.sns.fixture.UserEntityFixture;
import com.sideproject.sns.model.entity.LikeEntity;
import com.sideproject.sns.model.entity.PostEntity;
import com.sideproject.sns.model.entity.UserEntity;
import com.sideproject.sns.repository.LikeEntityRepository;
import com.sideproject.sns.repository.PostEntityRepository;
import com.sideproject.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @MockBean
    LikeEntityRepository likeEntityRepository;

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

    @Test
    void 포스트수정_성공() {
        String title = "title";
        String content = "content";
        String username = "username";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(1L, username, postId);
        UserEntity userEntity = UserEntityFixture.get(1L, username, "password");

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.createPost(title, content, username));
    }

    @Test
    void 포스트수정_포스트_존재_X() {
        String title = "title";
        String content = "content";
        String username = "username";
        Long postId = 1L;

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));

        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.modifyPost(title, content, username, postId));

        Assertions.assertEquals(ErrorCode.NO_DATA.name(), ex.getErrorCode().name());

    }

    @Test
    void 포스트수정_수정권한_X() {
        String title = "title";
        String content = "content";
        String username = "username";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(1L, username, postId);
        UserEntity userEntity = UserEntityFixture.get(2L, username, "password");

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));


        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.modifyPost(title, content, username, postId));

        Assertions.assertEquals(ErrorCode.NO_PERMISSION.name(), ex.getErrorCode().name());

    }

    @Test
    public void 포스트삭제_성공() throws Exception {
        // given
        String username = "username";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(1L, username, postId);
        UserEntity userEntity = postEntity.getUser();

        // when
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // then
        Assertions.assertDoesNotThrow(() -> postService.deletePost(postId, username));
    }

    @Test
    void 포스트삭제_로그인_x() {
        //given
        String username = "username";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(1L, username, postId);

        //when
        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.empty());

        //then
        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.deletePost(postId, username));


        verify(postEntityRepository, never()).delete(postEntity);
        Assertions.assertAll(
                () -> Assertions.assertEquals(ErrorCode.NO_DATA.name(), ex.getErrorCode().name()),
                () -> Assertions.assertEquals("존재하지 않는 데이터. username: 유저가 존재하지 않습니다", ex.getMessage())
        );

    }

    @Test
    void 포스트삭제_포스트_존재_x() {
        String username = "username";
        Long postId = 1L;

        //when
        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.findById(postId)).thenThrow(new CustomException(ErrorCode.NO_DATA, ""));

        //then
        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.deletePost(postId, username));
        Assertions.assertEquals(ErrorCode.NO_DATA.name(), ex.getErrorCode().name());
    }

    @Test
    void 포스트삭제_삭제요청유저_권한_x() {
        //given
        String username = "username";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(1L, username, postId);
        UserEntity userEntity = UserEntityFixture.get(2L, "real user", "password");

        //when
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        //then
        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.deletePost(postId, username));

        Assertions.assertEquals(ErrorCode.NO_PERMISSION.name(), ex.getErrorCode().name());

    }

    @Test
    void 피드목록_성공() {
        Pageable pageable = mock(Pageable.class);
        Page<PostEntity> page = mock(Page.class);

        when(postEntityRepository.findAll(pageable)).thenReturn(page);

        Assertions.assertDoesNotThrow(() -> postService.listPost(pageable));
    }

    @Test
    void 내_피드목록_성공() {
        Pageable pageable = mock(Pageable.class);
        Page<PostEntity> page = mock(Page.class);
        UserEntity userEntity = UserEntityFixture.get(1L, "username", "password");

        when(userEntityRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findAllByUser(userEntity, pageable)).thenReturn(page);

        Assertions.assertDoesNotThrow(() -> postService.listMyFeed(pageable, "username"));
    }

    @Test
    void 내_피드목록_로그인_X() {
        Pageable pageable = mock(Pageable.class);

        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.empty());

        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.listMyFeed(pageable, any()));
        Assertions.assertEquals(ErrorCode.NO_DATA.name(), ex.getErrorCode().name());

    }

    @Test
    public void 좋아요_성공() throws Exception {
        // given
        Long postId = 1L;
        String username = "username";

        UserEntity userEntity = UserEntityFixture.get(1L, username, "password");
        PostEntity postEntity = PostEntityFixture.get(1L, username, postId);

        // when
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // then
        Assertions.assertDoesNotThrow(() -> postService.likePost(postId, username));
    }

    @Test
    public void 좋아요_게시글_존재_x() throws Exception {
        // given
        Long postId = 1L;
        String username = "username";

        // when
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        // then
        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.likePost(postId, username));
        Assertions.assertEquals(ErrorCode.NO_DATA.name(), ex.getErrorCode().name());
        verify(likeEntityRepository, never()).save(any());
    }

    @Test
    public void 좋아요_이미_했음() throws Exception {
        // given
        Long postId = 1L;
        String username = "username";

        PostEntity postEntity = PostEntityFixture.get(1L, username, postId);
        UserEntity userEntity = UserEntityFixture.get(1L, username, "password");

        // when
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(likeEntityRepository.findByUserAndPost(userEntity, postEntity)).thenReturn(Optional.of(mock(LikeEntity.class)));

        // then
        CustomException ex = Assertions.assertThrows(CustomException.class, () -> postService.likePost(postId, username));
        Assertions.assertEquals(ErrorCode.DUPLICATED.name(), ex.getErrorCode().name());
        verify(likeEntityRepository, never()).save(any());
    }

}
