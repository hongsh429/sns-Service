package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.exception.ErrorCode;
import com.sideproject.sns.model.User;
import com.sideproject.sns.model.entity.UserEntity;
import com.sideproject.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String username, String password) {
        //회원가입하려는 username으로 회원가입된 user가 있는지 확인
        userEntityRepository.findByUsername(username).ifPresent(user -> {
            throw new CustomException(ErrorCode.DUPLICATED, String.format("%s: 이미 존재하는 아이디입니다", username));
        });

        // 회원가입 진행 = user를 db 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, password));

        return User.fromEntity(userEntity);
    }


    // 토큰을 반환해줄 것
    public String login(String username, String password) {
        // 회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NO_DATA, String.format("%s: 존재하지 않는 아이디입니다", username))
        );

        // 비밀번호 체크
        if (userEntity.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.NO_DATA, String.format("%s: 비밀번호가 일치하지 않습니다.", password));
        }

        // 토큰 생성

        return "";
    }
}
