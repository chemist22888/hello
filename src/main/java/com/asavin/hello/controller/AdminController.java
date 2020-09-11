package com.asavin.hello.controller;

import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import com.asavin.hello.service.PostService;
import com.asavin.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
//    @Autowired
//    Po userService;

    public List<User> getUsers(@RequestParam(defaultValue = "-1",required = false) Long lastId,@RequestParam(defaultValue = "-1",required = false) int quantity) {
        return userService.getUserWhereIdLessLimit(lastId, quantity);
    }
    @GetMapping("/getPosts")
    public List<Post> getAllPosts(@RequestParam(defaultValue = "-1") Long lastId,@RequestParam(defaultValue = "-1") int quantity){
        return postService.getPostWhereIdLessLimit(lastId, quantity);}

    @GetMapping("/getComments")
    public List<Coment> getComments(@RequestParam Long lastId,@RequestParam int quantity) {
        return postService.getComentWhereIdLessLimit(lastId,quantity);}
    @GetMapping("/getImages")

    public List<Image> getImages(@RequestParam(defaultValue = "-1") int lastId,@RequestParam(defaultValue = "-1") int quantity) {
        return postService.getAllImages();
    }

    public User getUser(Long id){return userService.findUserById(id);}
    public void dropUser(Long id){userService.deleteUserById(id);}
    public void dropPost(Long id){postService.deletePost(id);}
    public void dropComment(Long id){postService.deleteComment(id);}
    public void dropImage(Long id){postService.deleteImage(id);}
}
