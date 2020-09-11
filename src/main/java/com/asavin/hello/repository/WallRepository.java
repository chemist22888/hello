package com.asavin.hello.repository;

import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.Wall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WallRepository extends JpaRepository<Wall,Long> {
    @Query("select w.posts from Wall w where w in :ids ")
    List<Post>getPostByWalls(@Param("ids") Set<Wall> ids);
}
