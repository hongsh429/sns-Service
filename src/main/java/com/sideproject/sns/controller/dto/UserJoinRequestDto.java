package com.sideproject.sns.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJoinRequestDto {

    private String username;
    private String password;
}
