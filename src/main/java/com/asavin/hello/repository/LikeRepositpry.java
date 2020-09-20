package com.asavin.hello.repository;

import com.asavin.hello.entity.Like;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepositpry extends JpaRepository<Like,Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
}
