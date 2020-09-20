package com.asavin.hello.controller;

import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import com.asavin.hello.service.PostService;
import com.asavin.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

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
    @PostMapping("/dropPost")
    public void dropPost(@RequestParam("id") Long id){postService.deletePost(id);}
    @PostMapping("/dropComment")
    public void dropComment(@RequestParam Long id){postService.deleteComment(id);}
    @PostMapping("/dropImage")

    public void dropImage(@RequestParam Long id){postService.deleteImage(id);}
}
