package com.asavin.hello.service;

import com.asavin.hello.entity.*;
import com.asavin.hello.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepositpry postRepositpry;
    @Autowired
    EntityManager entityManager;
    @Autowired
    WallRepository wallRepository;
    @Autowired
    FriendRequestRepository friendRequestRepository;
    @Autowired
    ComentRepository comentRepository;
    @Autowired
    WebSocketService webSocketService;
    @Autowired
    LikeRepositpry likeRepositpry;
    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;
    @Autowired
    UserLastActivityRepository userLastActivityRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUserWhereIdLessLimit(Long id, int quantity) {
        if (id != -1l && quantity != -1)
            return userRepository.getUserWhereIdLess(id, PageRequest.of(0, quantity));
        else if (id != -1l && quantity == -1)
            return userRepository.getUserWhereIdLess(id);
        else if (id == -1l && quantity != -1)
            return userRepository.findAll(PageRequest.of(0, quantity)).getContent();
        else
            return userRepository.findAll();
    }

    @Override
    public List<User> getUserWhereIdLess(Long id) {
        return userRepository.getUserWhereIdLessLimit(id, -1);
    }

    @Override
    public List<User> getUserLimit(int quantity) {
        return userRepository.getUserWhereIdLessLimit(-1l, quantity);
    }

    @Override
    public User getMe() {
        System.out.println("service thread " + Thread.currentThread().getId());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByUserNameOrId(String id) {
        try {
            long uId = Long.parseLong(id);
            return findUserById(uId);
        } catch (NumberFormatException e) {
            return findUserByUserName(id);
        }
    }

    //TODO threadsafe - ?
    @Override
    public Post writePost(String text) {
        User me = getMe();

        Post post = new Post();
        post.setText(text);
        post.setWall(me.getWall());

        return postRepositpry.save(post);
    }

    @Transactional
    @Override
    public void makeFriends(User user1, User user2) {
        user1.
                getFriends()
                .add(user2);
        user2.getFriends().add(user1);

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Override
    public void unbound(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);

        unbound(user1, user2);
    }

    @Override
    @Transactional
    public void unbound(User user1, User user2) {
        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);

        System.out.println(user1.getFriends().size());

        userRepository.save(user1);
        userRepository.save(user2);

        try {
            webSocketService.sendFriendStatus(user1.getUsername(), user2, FriendRequest.DELETE_FRIEND);
            webSocketService.sendFriendStatus(user2.getUsername(), user1, FriendRequest.DELETE_FRIEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFriend(User user1, User user2) {
        return user1.getFriends().contains(user2) && user2.getFriends().contains(user1);
    }

    @Override
    public int friendStatus(Long user1Id, Long user2Id) {
        User user1 = findUserById(user1Id);
        User user2 = findUserById(user2Id);

        System.out.println("ids are" + user1.getFriends() + " " + user2.getFriends());

        int status = (user1.getFriends().contains(user2)
                && user2.getFriends().contains(user1)) ? 2 : 0;
        if (friendRequestRepository.findByFromAndTo(user1, user2).isPresent())
            status = 1;
        else if (friendRequestRepository.findByFromAndTo(user2, user1).isPresent())
            status = -1;


        return status;
    }

    @Override
    public List<Post> getNewsOfUsersFriend(User user) {
        return postRepositpry.orderPostsByTime(wallRepository.getPostByWalls(userRepository.newPosts(
                userRepository.getUsersIds(user.getFriends()))
        ));
    }

    @Override
    public void applyFriendRequest(String fromId, String toId) {
        User from = userRepository.findByUsername(fromId);
        User to = userRepository.findByUsername(toId);
        ;

        applyFriendRequest(from, to);
    }

    @Override
    public void cancelFriendRequest(String fromId, String toId) {
        User from = userRepository.findByUsername(fromId);
        User to = userRepository.findByUsername(toId);

        cancelFriendRequest(from, to);
    }

    @Override
    public void applyFriendRequest(User from, User to) {
        int status;
        if (friendRequestRepository.findByFromAndTo(to, from).isPresent()) {
            makeFriends(from, to);
            status = FriendRequest.ACCEPT_FRIEND_REQUEST;
        } else {
            FriendRequest friendRequest = new FriendRequest(from, to);
            friendRequestRepository.save(friendRequest);
            status = FriendRequest.APPLY_FRIEND_REQUEST;
        }
        try {
            webSocketService.sendFriendStatus(to.getUsername(), from, status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void acceptFriendRequest(User from, User to) {
        friendRequestRepository.findByFromAndTo(from, to).ifPresent(request -> {
            System.out.println("present");
            friendRequestRepository.delete(request);
            makeFriends(from, to);

            try {
                webSocketService.sendFriendStatus(from.getUsername(), to, FriendRequest.ACCEPT_FRIEND_REQUEST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void declineFriendRequest(User from, User to) {
        FriendRequest friendRequest = friendRequestRepository.findByFromAndTo(from, to).get();
        friendRequestRepository.delete(friendRequest);

        try {
            webSocketService.sendFriendStatus(from.getUsername(), to, FriendRequest.DECLINE_FRIEND_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelFriendRequest(User from, User to) {
        Optional<FriendRequest> friendRequest = friendRequestRepository.findByFromAndTo(from, to);

        if (friendRequest.isPresent()) {
            friendRequestRepository.delete(friendRequest.get());
            try {
                webSocketService.sendFriendStatus(to.getUsername(), from, FriendRequest.CANCEL_FRIEND_REQUEST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeComment(User user, Post post, String text) {
        Coment coment = new Coment(user, post, text);
        comentRepository.save(coment);

    }

    @Override
    public List<Coment> getUsersComents(User user) {
        return comentRepository.findByUser(user);
    }

    @Override
    public void likePost(Long userId, Long postId) {
        Like like = new Like(new User(userId), new Post(postId));
        likeRepositpry.save(like);
    }

    @Override
    public void applyRegistration(String username, String password, String email) {
        Registration registration = new Registration(email, username, password);
        registration.setRegistrationTime(Instant.now());

        registration = registrationRepository.save(registration);

        String text = "To confirm account go to http://localhost:8080/register?confirmId=" + registration.getUuid().toString();
        emailService.send("registration", text, email);
    }

    @Override
    public void confirmRegistration(String uuid) {
        System.out.println(uuid);
        registrationRepository.findById(UUID.fromString(uuid)).ifPresent(registration -> {
            User user = new User();
            Wall wall = new Wall();

            user.setPassword(passwordEncoder.encode(registration.getPassword()));
            user.setUsername(registration.getUsername());
            user.setEmail(registration.getEmail());

            wall = wallRepository.save(wall);
            user.setWall(wall);

            userRepository.save(user);
            registrationRepository.delete(registration);
        });
    }

    @Override
    public void pingOnline(User user) {
        userLastActivityRepository.findByUser(user).ifPresent(currentActivity -> {
            currentActivity.setLastActivityTime(Instant.now());

            userLastActivityRepository.save(currentActivity);
        });
    }

    @Override
    public boolean isOnline(User user) {
        if (userLastActivityRepository.findByUser(user).isPresent()) {
            UserLastActivity lastActivity = userLastActivityRepository.findByUser(user).get();
            System.out.println(Duration.between(lastActivity.getLastActivityTime(), Instant.now()).abs().toMinutes());
            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

            System.out.println(DATE_TIME_FORMATTER.format(Instant.now()));
            System.out.println(DATE_TIME_FORMATTER.format(lastActivity.getLastActivityTime()));

            return Duration.between(lastActivity.getLastActivityTime(), Instant.now()).abs().toMinutes() < 3;
        }
        return false;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserByUsername(String username) {

    }

    @Override
    public void acceptFriendRequest(String fromId, String toId) {
        User from = userRepository.findByUsername(fromId);
        User to = userRepository.findByUsername(toId);

        acceptFriendRequest(from, to);
    }

    @Override
    public void declineFriendRequest(String fromId, String toId) {
        User from = userRepository.findByUsername(fromId);
        User to = userRepository.findByUsername(toId);

        System.out.println(fromId + ' ' + toId);
        declineFriendRequest(from, to);

    }
//    private User getUserB
}
