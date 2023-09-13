package com.sideproject.sns.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.sns.controller.response.Response;
import com.sideproject.sns.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(ErrorCode.INVALID_DATA.getStatus().value());
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(Response.error(
                                ErrorCode.INVALID_DATA.name(),
                                String.format("%s 유효하지 않는 토큰", ErrorCode.INVALID_DATA.getMessage())
                        )
                )
        );
    }
}
