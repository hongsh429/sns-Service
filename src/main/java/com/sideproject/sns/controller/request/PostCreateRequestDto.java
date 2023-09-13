package com.sideproject.sns.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor // 기본적으로 넣어줄때 기본생성자를 기준으로 넣어준다는 것.
public class PostCreateRequestDto {
    private String title;
    private String content;
}
