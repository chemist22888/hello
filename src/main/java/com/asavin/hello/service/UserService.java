package com.asavin.hello.service;

import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    List<User> getUserWhereIdLessLimit(Long id,int quantity);
    List<User> getUserWhereIdLess(Long id);
    List<User> getUserLimit(int quantity);
    User getMe();
    User findUserById(Long id);
    User findUserByUserName(String username);
    User findUserByUserNameOrId(String username);
    Post writePost(Post post);
    void makeFriends(User user1,User user2);
    void unbound(String id1,String id2);
    void unbound(User user1,User user2);
    boolean isFriend(User user1,User user2);
    int friendStatus(Long user1Id,Long user2Id);
    List<Post>getNewsOfUsersFriend(User user);
    void applyFriendRequest(String from,String to);
    void acceptFriendRequest(String from,String to);
    void declineFriendRequest(String from,String to);
    void cancelFriendRequest(String from,String to);
    void applyFriendRequest(User from,User to);
    void acceptFriendRequest(User from,User to);
    void declineFriendRequest(User from,User to);
    void cancelFriendRequest(User from,User to);
    void writeComment(User user,Post post,String text);
    List<Coment>getUsersComents(User user);
    void likePost(User user,Long postId);
    void applyRegistration(String username, String password, String email);
    void confirmRegistration(String uuid);
    void pingOnline(User user);
    boolean isOnline(User user);
    void deleteUser(User user);
    void deleteUserById(Long id);
    void deleteUserByUsername(String username);
    void setAvatar(User user, Image image);
}
