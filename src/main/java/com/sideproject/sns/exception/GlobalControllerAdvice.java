package com.sideproject.sns.exception;


import com.sideproject.sns.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {


    @ExceptionHandler({CustomException.class})
    public ResponseEntity<?> applicationErrorHandler(CustomException ex) {
        log.error("Error occurs {}", ex.toString());
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(Response.error(ex.getErrorCode().name(), ex.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> runtimeHandler(RuntimeException ex) {
        log.error("Error occurs {}", ex.toString());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name(), ex.getMessage()));
    }

}
