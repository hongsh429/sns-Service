package com.sideproject.sns.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
}
