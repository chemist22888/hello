package com.asavin.hello.controller;

import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.FriendRequestRepository;
import com.asavin.hello.repository.PostRepositpry;
import com.asavin.hello.repository.UserRepository;
import com.asavin.hello.repository.WallRepository;
import com.asavin.hello.service.JsonService;
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
    @GetMapping("/image/{name}")
    public void getImageAsByteArray(@PathVariable("name")String name, HttpServletResponse response) throws IOException {
        File initialFile = new File("D://images/"+name+".png");
        InputStream in = new FileInputStream(initialFile);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
    @PostMapping("/loadImage")
    public void loadImageAsByteArray(@RequestBody String recievedFile) throws IOException {
        System.out.println(recievedFile.substring(0,30));
        try {
            recievedFile = recievedFile.substring(recievedFile.indexOf(",") + 1);
            byte[] decodedString = Base64.decodeBase64(recievedFile.getBytes());
//            System.out.println(new String(decodedString));

            File file = new File("D://1.png");
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(decodedString);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//
//        File initialFile = new File("D://images/"+name+".jpg");
//        InputStream in = new FileInputStream(initialFile);
//        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        IOUtils.copy(in, response.getOutputStream());
    }
    @PostMapping("/likePost")
    public void likePost(@RequestParam("postId") Long postId){
        userService.likePost(userService.getMe().getId(),postId);
    }
    //    @Autowired
//    WebSocketService webSocketService;
//    @GetMapping("/news")
//    @JsonView(UserViewJson.UserFullDetails.class)
//    public List<Post> getNewPosts() {
//        return userService.getMe().getFriends();
//    }
}
