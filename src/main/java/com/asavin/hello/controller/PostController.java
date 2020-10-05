package com.asavin.hello.controller;

import com.asavin.hello.annotation.AdditionalProperties;
import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.*;
import com.asavin.hello.service.JsonService;
import com.asavin.hello.service.PostService;
import com.asavin.hello.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class PostController {
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
    ImageRepository imageRepository;
    @Autowired
    PostService postService;
    @Value("${imagesPath}")
    String imagesPath;
    @PostMapping("/loadImage")
    public Long loadImageAsByteArray(@RequestBody String recievedFile) {
        System.out.println(recievedFile.substring(0,30));
        try {
            String type = recievedFile.substring(11,14);
            System.out.println("typeimage is "+type);
            recievedFile = recievedFile.substring(recievedFile.indexOf(",") + 1);
            byte[] decodedString = Base64.decodeBase64(recievedFile.getBytes());

            Image image = postService.saveImage(decodedString,type);
            return image.getId();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1l;
        }
    }
    @AdditionalProperties(properties = AdditionalProperties.Properties.likeStatus)
    @GetMapping("/posts")
    public Object posts(){
        return userService.getMe().getWall().getPosts();
    }
    @PostMapping("/likePost")
    public void likePost(@RequestParam("postId") Long postId){
        userService.likePost(userService.getMe(),postId);
    }
    @PostMapping("/unlikePost")
    public void unlikePost(@RequestParam("postId") Long postId) {
        userService.unlikePost(userService.getMe(),postId);
    }

    @PostMapping("/isLiked")
    public boolean isLiked(@RequestParam("postId") Long postId){
        return postService.isLiked(new Post(postId), userService.getMe());
    }

    @PostMapping("/writeComent")
    @CrossOrigin(origins = "http://localhost:4200")
    public Coment writeComent(@RequestParam Long postId, @RequestParam String text) {
        return postService.writeComent(userService.getMe(),text,new Post(postId));
    }
    @GetMapping("/image/{id}")
    public void getImageAsByteArray(@PathVariable("id")Long id, HttpServletResponse response) throws IOException {
        String name = imageRepository.findById(id).get().getName();

        File initialFile = new File(imagesPath+"/"+name);
        InputStream in = new FileInputStream(initialFile);
        String imageType = name.substring(name.lastIndexOf('.'));
        if(imageType.equals("png"))
            imageType = MediaType.IMAGE_PNG_VALUE;
        else if(imageType.equals("jpg"))
            imageType = MediaType.IMAGE_JPEG_VALUE;
        response.setContentType(imageType);
        IOUtils.copy(in, response.getOutputStream());
    }
}
