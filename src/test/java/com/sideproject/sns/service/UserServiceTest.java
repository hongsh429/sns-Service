package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.fixture.UserEntityFixture;
import com.sideproject.sns.model.entity.UserEntity;
import com.sideproject.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @Test
    void 회원가입이_정상적으로_동작() {

        String username = "username";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(Optional.of(userEntity));

        Assertions.assertDoesNotThrow(()-> userService.join(username, password));
    }

    @Test
    void 회원가입_username_중복() {

        String username = "username";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(userEntity));

        Assertions.assertThrows(CustomException.class, ()-> userService.join(username, password));
    }

    @Test
    void 로그인_정상적으로_동작() {

        String username = "username";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        //mocking
        // 실제 로직에서는 여기서 나온 결과를 가지고 if(~) 로직이 구현되어 있다. 이를 위해서 fixture를 하나 만들기!
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        Assertions.assertDoesNotThrow(()-> userService.login(username, password));
    }

    @Test
    void 로그인_username_등록안됨() {

        String username = "username";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(CustomException.class, ()-> userService.login(username, password));
    }

    @Test
    void 로그인_password_틀림() {

        String username = "username";
        String password = "password";
        String wrongPassword = "wrong password";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));


        Assertions.assertThrows(CustomException.class, ()-> userService.login(username, wrongPassword));
    }
}
