package com.asavin.hello.service;

import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

public interface PostService {
    void deletePost(Long id);
    void deleteComment(Long id);
    void deleteImage(Long id);
    List<Post> getAllPosts();
    List<Coment> getAllComments();
    List<Image> getAllImages();
    List<Post> getPostWhereIdLessLimit(Long id, int quantity);
    List<Coment> getComentWhereIdLessLimit(Long id, int quantity);
    List<Image> getImageWhereIdLessLimit(Long id, int quantity);
    Coment writeComent(User user,String text,Post post);
    Image saveImage(byte [] file,String type);
    BufferedImage squareImare(byte[] bytes);
    boolean isLiked(Post post,User user);
}
