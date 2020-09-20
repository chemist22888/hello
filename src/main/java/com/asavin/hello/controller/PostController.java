package com.asavin.hello.controller;

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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    @GetMapping("/image/{id}")
    public void getImageAsByteArray(@PathVariable("id")Long id, HttpServletResponse response) throws IOException {
        String name = imageRepository.findById(id).get().getName();
        File initialFile = new File("C://12/"+name);
        InputStream in = new FileInputStream(initialFile);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
    @PostMapping("/loadImage")
    public String loadImageAsByteArray(@RequestBody String recievedFile) throws IOException {
        System.out.println(recievedFile.substring(0,30));
        try {
            recievedFile = recievedFile.substring(recievedFile.indexOf(",") + 1);
            byte[] decodedString = Base64.decodeBase64(recievedFile.getBytes());

            Image image = imageRepository.save(new Image());
            image.setName(image.getId()+".png");
            imageRepository.save(image);
            File file = new File("C://12/"+image.getName());

            try (OutputStream os = new FileOutputStream(file)) {
                os.write(decodedString);
                return image.getId()+"";
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return "error";

        }
    }
    @PostMapping("/likePost")
    public void likePost(@RequestParam("postId") Long postId){
        userService.likePost(userService.getMe(),postId);
    }

    @PostMapping("/writeComent")
    @CrossOrigin(origins = "http://localhost:4200")
    public Coment writeComent(@RequestParam Long postId, @RequestParam String text) {
        return postService.writeComent(userService.getMe(),text,new Post(postId));
    }
}
