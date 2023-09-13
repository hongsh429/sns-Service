package com.sideproject.sns.service;


import com.sideproject.sns.exception.CustomException;
import com.sideproject.sns.exception.ErrorCode;
import com.sideproject.sns.fixture.UserEntityFixture;
import com.sideproject.sns.model.entity.UserEntity;
import com.sideproject.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    BCryptPasswordEncoder encoder;

    @MockBean
    UserEntityRepository userEntityRepository;

    @Test
    void 회원가입이_정상적으로_동작() {

        String username = "username";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(1L, username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(userEntity);

        Assertions.assertDoesNotThrow(() -> userService.join(username, password));
    }

    @Test
    void 회원가입_username_중복() {

        String username = "username";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(1L, username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(userEntity));

        CustomException ex = Assertions.assertThrows(CustomException.class, () -> userService.join(username, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED, ex.getErrorCode());
    }

    @Test
    void 로그인_정상적으로_동작() {

        String username = "username";
        String password = "password";
        String encodedPassword = "encoded password";

        UserEntity userEntity = UserEntityFixture.get(1L, username, password);

        //mocking
        // 실제 로직에서는 여기서 나온 결과를 가지고 if(~) 로직이 구현되어 있다. 이를 위해서 fixture를 하나 만들기!
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(encoder.matches(any(), any())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(username, password));
    }

    @Test
    void 로그인_username_등록안됨() {

        String username = "username";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(1L, username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        CustomException ex = Assertions.assertThrows(CustomException.class, () -> userService.login(username, password));
        Assertions.assertEquals(ErrorCode.NO_DATA, ex.getErrorCode());
    }

    @Test
    void 로그인_password_틀림() {

        String username = "username";
        String password = "password";
        String wrongPassword = "wrong password";

        UserEntity userEntity = UserEntityFixture.get(1L, username, password);

        //mocking
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));


        CustomException ex = Assertions.assertThrows(CustomException.class, () -> userService.login(username, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_DATA, ex.getErrorCode());
    }
}
