package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
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
        Optional<UserEntity> userEntity = userEntityRepository.findByUsername(username);


        // 회원가입 진행 = user를 db 등록
        userEntityRepository.save(new UserEntity());

        return new User();
    }


    // 토큰을 반환해줄 것
    public String login(String username, String password) {
        // 회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new CustomException());

        // 비밀번호 체크
        if (userEntity.getPassword().equals(password)) {
            throw new CustomException();
        }

        // 토큰 생성

        return "";
    }
}
