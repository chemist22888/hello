package com.asavin.hello.repository;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.User;
import com.asavin.hello.entity.Wall;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedEntityGraph;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    @EntityGraph(value = "graph.Full")
    @Query("select u from User u where u.username=:username")
    User findByUsernameFull(@Param("username") String username);
    @Query("select u.wall from User u where u.id in :ids")
    Set<Wall>newPosts(@Param("ids") Set<Long> ids);
    @Query("select u.id from User u where u.username = :username")
    Long findIdByUsername(@Param("username") String username);
    @Query("select u.id from User u where u in :users")
    Set<Long>getUsersIds(@Param("users") Set<User>users);
    void deleteUserByUsername(String username);
    @Query("select u from User u where u.id < :id")
    List<User> getUserWhereIdLess(@Param("id")Long id, Pageable pageable);
    @Query("select u from User u where u.id < :id")
    List<User> getUserWhereIdLess(@Param("id")Long id);
    @Query("select u.friends from User u where u.id=:id")
    Set<User> findFriendsById(@Param("id")Long id);
    default List<User> getUserWhereIdLessLimit(Long id,int quantity) {
        if(id!=-1l && quantity!=-1)
            return getUserWhereIdLess(id,PageRequest.of(0,quantity));
        else if(id!=-1l && quantity == -1)
            return getUserWhereIdLess(id);
        else if(id==-1l && quantity != -1)
            return findAll(PageRequest.of(0,quantity)).getContent();
        else
            return findAll();
    }
//    @Query("select f from User f where f.id member of select me.friends from User me where me.id=?1")
//    Set<User> getMe(Long id);
}
