package com.asavin.hello.repository;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.FriendRequest;
import com.asavin.hello.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest,Long> {
//    FriendRequest findByFrom(User user);
//    FriendRequest findByTo(User user);
    Optional<FriendRequest> findByFromAndTo(User from, User to);

}
