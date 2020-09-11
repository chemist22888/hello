package com.asavin.hello.controller;

import com.asavin.hello.entity.*;
import com.asavin.hello.annotation.AdditionalProperties;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.*;
import com.asavin.hello.service.JsonService;
import com.asavin.hello.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.asavin.hello.annotation.AdditionalProperties.Properties;


import java.util.ArrayList;
import java.util.List;

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
    //    @Autowired
//    WebSocketService webSocketService;
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
        for(Post p:user.getWall().getPosts()){
            System.out.println(p.getId()+" "+p.getComents().size());
        }
        return user;
    }

    @GetMapping("/me")
    @JsonView(UserViewJson.UserFullDetails.class)
    @AdditionalProperties(properties = {Properties.friendStatus,Properties.onlineStatus})
//    @FriendStatus(fields = {})
    public Object getMe() {
//        imageRepository.save(new Image());
        return (userService.getMe());
    }

    @PostMapping("/write")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public Post write(@RequestParam String text) {
        return userService.writePost(text);
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


    @PostMapping("/writeComent")
    @CrossOrigin(origins = "http://localhost:4200")
    public void writeComent(Long postId,String text) {
        Post post = postRepositpry.findById(postId).get();
        comentRepository.save(new Coment(userService.getMe(),post,text));
    }
    @JsonView(UserViewJson.UserInChatDetails.class)

    @PostMapping("/myComments")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Coment> getMyComments() {
        return comentRepository.findByUser(userService.getMe());
    }

    @PostMapping("/wtf")
    @CrossOrigin(origins = "http://localhost:4200")
    public void wtf() {
        System.out.println(userService.getMe().getUsername());
    }

    @PostMapping("/bond")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public User makeFriends(String id) throws Throwable {
        User newFriend = userService.findUserByUserNameOrId(id);
        System.out.println(objectMapper.writeValueAsString(getMe()));

        userService.makeFriends(userService.getMe(), newFriend);
        return newFriend;
    }

    @PostMapping("/unbond")
    @JsonView(UserViewJson.UserInChatDetails.class)
    public void unbond(@RequestParam String username) throws Throwable {
//        User newFriend = getUserById(id);


        userService.unbound(userService.getMe().getUsername(), username);
//        webSocketService.sendFriendStatus(username,userService.getMe().getUsername(),0);

//        return  newFriend;
    }

    @PostMapping("applyFriendReq")
    public void applyRequest(@RequestParam String username) {
        userService.applyFriendRequest(userService.getMe().getUsername(), username);
//        webSocketService.sendFriendStatus(username,userService.getMe().getUsername(),-1);
    }

    @PostMapping("acceptFriendReq")
    public void acceptRequest(@RequestParam String username) {
        userService.acceptFriendRequest(username,userService.getMe().getUsername());
//        webSocketService.sendFriendStatus(username,userService.getMe().getUsername(),2);

    }

    @PostMapping("declineFriendReq")
    public void declineRequest(@RequestParam String username) {
//        webSocketService.sendFriendStatus(username,userService.getMe().getUsername(),0);

        userService.declineFriendRequest(username, userService.getMe().getUsername());
    }

    @PostMapping("cancelFriendReq")
    public void cancelRequest(@RequestParam String username) {
        userService.cancelFriendRequest(userService.getMe().getUsername(),username);
    }

//@RequestMapping(method = RequestMethod.OPTIONS,path = "foo")
//void ofoo(){}
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


//    private User getUserById(String id) {
//        User newFriend;
//        try {
//            long uId = Long.parseLong(id);
//            newFriend = userService.findUserById(uId);
//            System.out.println("longid is " + id);
//        } catch (NumberFormatException e) {
//            newFriend = userService.findUserByUserName(id);
//            System.out.println("username is " + id);
//        }
//        return newFriend;
//    }
}
