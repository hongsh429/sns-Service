package com.sideproject.sns.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.sns.controller.dto.UserJoinRequestDto;
import com.sideproject.sns.controller.dto.UserLoginRequestDto;
import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.model.User;
import com.sideproject.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    public void 회원가입_성공() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.join(username, password)).thenReturn(mock(User.class));

        //요청
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserJoinRequestDto(username, password)))
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void 회원가입_실패_중복된아이디() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.join(username, password)).thenThrow(new CustomException());

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserJoinRequestDto(username, password)))
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void 로그인_성공() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserLoginRequestDto(username, password)))
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void 로그인_실패_회원가입_안된_username() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenThrow(new CustomException());

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserLoginRequestDto(username, password)))
                )
                .andDo(print())
                .andExpect(status().isNotFound());  // 404

    }

    @Test
    public void 로그인_실패_password_잘못_입력() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenThrow(new CustomException());

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserLoginRequestDto(username, password)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());  // 401

    }
}
