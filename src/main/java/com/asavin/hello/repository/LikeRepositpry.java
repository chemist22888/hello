package com.asavin.hello.repository;

import com.asavin.hello.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepositpry extends JpaRepository<Like,Long> {
}
