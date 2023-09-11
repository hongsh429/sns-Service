package com.sideproject.sns.model.entity;

import com.sideproject.sns.model.BaseAuditing;
import com.sideproject.sns.model.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static UserEntity of(String username, String password) {
        return UserEntity.builder()
                .username(username)
                .password(password)
                .role(UserRole.USER)
                .build();
    }
}
