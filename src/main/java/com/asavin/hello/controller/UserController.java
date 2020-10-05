package com.asavin.hello.controller;

import com.asavin.hello.entity.*;
import com.asavin.hello.annotation.AdditionalProperties;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.*;
import com.asavin.hello.service.JsonService;
import com.asavin.hello.service.PostService;
import com.asavin.hello.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;

import static com.asavin.hello.annotation.AdditionalProperties.Properties;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepositpry postRepositpry;
    @Autowired
    WallRepository wallRepository;
    @Autowired
    JsonService jsonService;
    @Autowired
    FriendRequestRepository friendRequestRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ComentRepository comentRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    PostService postService;
    @GetMapping("/users")
    @JsonView(UserViewJson.UserFullDetails.class)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @JsonView(UserViewJson.UserFullDetails.class)
    @GetMapping("/user/{id}")
    @AdditionalProperties(properties = Properties.friendStatus)
    public Object getUser(@PathVariable String id) {
        User user = userService.findUserByUserNameOrId(id);

        return user;
    }

    @GetMapping("/me")
    @JsonView(UserViewJson.UserFullDetails.class)
    @AdditionalProperties(properties = {Properties.friendStatus})
    public Object getMe() {
        return (userService.getMe());
    }
    @GetMapping("/status")
    public Object status(@RequestParam Long userId) {
        return userService.friendStatus(userService.getMe().getId(),userId);
    }
    @GetMapping("/meShort")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public Object getMeShort() {
        return userService.getMe();
    }
    @GetMapping("/username")
    public String username(){
        return userService.getUsername();
    }
    @GetMapping("/myId")
    public Long id(){
        return userService.getId();
    }

    @PostMapping("/write")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public Post write(@RequestBody Post post) {
        Post post1 = userService.writePost(post);
        return post1;
    }
    @CrossOrigin(origins = "http://localhost:4200")

    @GetMapping("/login")
    public String login(@RequestHeader String authorization) {
        return "{\"test\":\""+authorization+"\"}";
    }

    @GetMapping("/friends")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public Object friends() throws Exception {
        List<User> res = new ArrayList<>();
        res.addAll(userService.getMe().getFriends());
        return (res);
    }
    @JsonView(UserViewJson.UserInChatDetails.class)
    @PostMapping("/myComments")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Coment> getMyComments() {
        return comentRepository.findByUser(userService.getMe());
    }


    @PostMapping("/bond")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public User makeFriends(String id) throws Throwable {
        User newFriend = userService.findUserByUserNameOrId(id);

        userService.makeFriends(userService.getMe(), newFriend);
        return newFriend;
    }

    @PostMapping("/unbond")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public void unbond(@RequestParam String username) throws Throwable {
        userService.unbound(userService.getMe().getUsername(), username);
    }

    @PostMapping("applyFriendReq")
    public void applyRequest(@RequestParam String username) {
        userService.applyFriendRequest(userService.getMe().getUsername(), username);
    }

    @PostMapping("acceptFriendReq")
    public void acceptRequest(@RequestParam String username) {
        userService.acceptFriendRequest(username,userService.getMe().getUsername());
    }
    @PostMapping("declineFriendReq")
    public void declineRequest(@RequestParam String username) {
        userService.declineFriendRequest(username, userService.getMe().getUsername());
    }

    @PostMapping("cancelFriendReq")
    public void cancelRequest(@RequestParam String username) {
        userService.cancelFriendRequest(userService.getMe().getUsername(),username);
    }
    @GetMapping("/testBd")
    public List<Post> test() {
        return userService.getNewsOfUsersFriend(userService.getMe());
    }
    @GetMapping("foo")
    public String foo(@RequestHeader String foo){
        return "{\"test\":\""+foo+"\"}";
    }

    @PostMapping("confirmRegistration")
    public void confirmRegistration(@RequestParam("id") String id){
        userService.confirmRegistration(id);
    }

    @PostMapping("registerUser")
    public void registrateUser(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("email") String email){
        userService.applyRegistration(username,password,email);
    }
    @GetMapping("/pingOnline")
    public void pingOnline() {
        userService.pingOnline(userService.getMe());
    }

    @PostMapping("/loadAvatar")
    public Long loadAvatar(@RequestBody String recievedFile) {
        String type = recievedFile.substring(11,14);
        recievedFile = recievedFile.substring(recievedFile.indexOf(",") + 1);
        return userService.loadAvatar(recievedFile,type,userService.getMe());

    }
    @JsonView(UserViewJson.UserInChatDetails.class)
    @GetMapping("/requestsFrom")
    public Set<User> getDirectedRequestFrom(){
        return userService.getDirectedRequestFrom(userService.getMe());
    }
    @JsonView(UserViewJson.UserInChatDetails.class)
    @GetMapping("/requestsTo")
    public Set<User> getDirectedRequestTo(){
        return userService.getDirectedRequestTo(userService.getMe());
    }
}