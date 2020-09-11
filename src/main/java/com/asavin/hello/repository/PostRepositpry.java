package com.asavin.hello.repository;

import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.Wall;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositpry extends JpaRepository<Post,Long> {
    @Query("select p from Post p where p in :posts order by p.id desc ")
    List<Post> orderPostsById(@Param("posts") List<Post> posts);

    @Query("select p from Post p where p in :posts order by p.creationDate desc  ")
    List<Post> orderPostsByTime(@Param("posts") List<Post> posts);

    @Query("select p from Post p where p.id < :id order by p.id desc")
    List<Post> getPostWhereIdLess(@Param("id")Long id, Pageable pageable);
    @Query("select p from Post p where p.id < :id order by p.id desc")
    List<Post> getPostWhereIdLess(@Param("id")Long id);

    @Query("select p from Post p order by p.id desc ")
    List<Post> findAllDesc();

    @Query("select p from Post p order by p.id desc ")
    List<Post> findAllDesc(Pageable pageable);

    //    default List<Post> getUserWhereIdLessLimit(Long id,int quantity) {
//        if(id!=-1 && quantity!=-1)
//            return getPostWhereIdLess(id, PageRequest.of(0,quantity));
//        else if(id!=-1 && quantity == -1)
//            return getPostWhereIdLess(id);
//        else if(id==-1 && quantity != -1)
//            return findAll(PageRequest.of(0,quantity)).getContent();
//        else
//            return findAll();
//    }
}
