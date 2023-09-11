package com.sideproject.sns.controller;


import com.sideproject.sns.controller.request.UserJoinRequestDto;
import com.sideproject.sns.controller.request.UserLoginRequestDto;
import com.sideproject.sns.controller.response.Response;
import com.sideproject.sns.controller.response.UserJoinResponseDto;
import com.sideproject.sns.controller.response.UserLoginResponseDto;
import com.sideproject.sns.model.User;
import com.sideproject.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponseDto> join(@RequestBody UserJoinRequestDto requestDto) {
        User user = userService.join(requestDto.getUsername(), requestDto.getPassword());
        return Response.success(UserJoinResponseDto.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto) {
        String token = userService.login(requestDto.getUsername(), requestDto.getPassword());
        return Response.success(new UserLoginResponseDto(token));
    }

}
