package com.asavin.hello.repository;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.FriendRequest;
import com.asavin.hello.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest,Long> {
    Optional<FriendRequest> findByFromAndTo(User from, User to);
    @Query("select r.from from FriendRequest r where r.to=:to")
    Set<User> findDirectsTo(@Param("to") User to);
    @Query("select r.to from FriendRequest r where r.from=:from")
    Set<User> findDirectsFrom(@Param("from") User from);

}
