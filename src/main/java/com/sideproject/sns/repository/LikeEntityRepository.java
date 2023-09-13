package com.sideproject.sns.repository;

import com.sideproject.sns.model.entity.LikeEntity;
import com.sideproject.sns.model.entity.PostEntity;
import com.sideproject.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndPost(UserEntity userEntity, PostEntity postEntity);
}
