package com.sideproject.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED(HttpStatus.CONFLICT, "데이터 중복"),
    NO_DATA(HttpStatus.BAD_REQUEST, "존재하지 않는 데이터"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),
    INVALID_DATA(HttpStatus.BAD_REQUEST, "일치하지 않는 데이터"),
    ;

    private final HttpStatus status;
    private final String message;
}
