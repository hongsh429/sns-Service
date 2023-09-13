package com.sideproject.sns.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.sns.controller.request.PostCreateRequestDto;
import com.sideproject.sns.controller.request.PostModifyRequestDto;
import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.exception.ErrorCode;
import com.sideproject.sns.fixture.PostEntityFixture;
import com.sideproject.sns.model.Post;
import com.sideproject.sns.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser // 가입한 유저
    void 포스트작성_성공() throws Exception {
        String title = "title";
        String content = "content";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PostCreateRequestDto(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser // 익명의 유저
    void 포스트작성_로그인_X() throws Exception {
        String title = "title";
        String content = "content";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PostCreateRequestDto(title, content)))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser // 가입한 유저
    void 포스트수정_성공() throws Exception {
        String title = "title";
        String content = "content";

        when(postService.modifyPost(eq(title), eq(content), any(), any()))
                // 실제로 controller에서는 반환된 결과(Post.class)를 가지고 PostModifyResponseDto를 만드는 동작을 하는 과정이 있다.
                // 이 과정에서 mocking을 하게 되었을 때, 문제가 발생
                .thenReturn(Post.fromEntity(PostEntityFixture.get(1L, "username", 1L)));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PostModifyRequestDto(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    void 포스트수정_로그인_X() throws Exception {
        String title = "title";
        String content = "content";

        mockMvc.perform(post("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PostModifyRequestDto(title, content)))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void 포스트수정_로그인_o_내가_쓴_글_x() throws Exception {
        String title = "title";
        String content = "content";


        // mocking
        // argumentMatcher를 사용할 때는, 메서드의 모든 인자에 사용하던가, 아니면 전부 실제 값을 넣어주던가. 둘중 하나로 해야한다!!!
        doThrow(new CustomException(ErrorCode.NO_PERMISSION, ""))
                .when(postService).modifyPost(eq(title), eq(content), any(), eq(1L));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PostModifyRequestDto(title, content)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트수정_로그인_o_글이_존재_x() throws Exception {
        String title = "title";
        String content = "content";

        //mocking
        doThrow(new CustomException(ErrorCode.NO_DATA, ""))
                .when(postService).modifyPost(eq(title), eq(content), any(), eq(1L));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PostModifyRequestDto(title, content)))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void 포스트삭제_성공() throws Exception {
        // void return시에는??

        mockMvc.perform(delete("/api/v1/posts/1")
//                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithAnonymousUser
    void 포스트삭제_로그인_x() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(postService, never()).deletePost(any(), any());
    }

    @Test
    @WithMockUser
    void 포스트삭제_작성자_삭제요청자_불일치() throws Exception {

        //mocking
        doThrow(new CustomException(ErrorCode.NO_PERMISSION, "")).when(postService).deletePost(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void 포스트삭제_포스트_존재_x() throws Exception {

        //mocking
        // void로 결과를 작성했기 때문에, doThrow로 작성하는 것이 좋음
        doThrow(new CustomException(ErrorCode.NO_DATA, "")).when(postService).deletePost(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
//                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void 피드목록_전체_성공_로그인_O() throws Exception {
        //mocking
        when(postService.listPost(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 피드목록_전체_성공_로그인_X() throws Exception {
        //mocking
        when(postService.listPost(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 나의_피드목록_전체_성공_로그인_O() throws Exception {
        //mocking
        Page<Post> page = mock(Page.class);
        when(postService.listMyFeed(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/posts/my-feed")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 나의_피드목록_전체_성공_로그인_X() throws Exception {
        //mocking
        when(postService.listMyFeed(any(), any())).thenThrow(new CustomException(ErrorCode.NO_DATA, ""));

        mockMvc.perform(get("/api/v1/posts/my-feed")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void 좋아요_로그인_o() throws Exception {

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

                verify(postService, times(1)).likePost(eq(1L), any());
    }

    @Test
    @WithAnonymousUser
    void 좋아요_로그인_x() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, never()).likePost(eq(1L), any());
    }

    @Test
    @WithMockUser
    void 좋아요_게시글_x() throws Exception {

        doThrow(new CustomException(ErrorCode.NO_DATA, "")).when(postService).likePost(eq(1L), any());

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
