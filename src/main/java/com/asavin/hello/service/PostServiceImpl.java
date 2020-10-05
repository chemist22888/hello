package com.asavin.hello.service;

import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import com.asavin.hello.repository.ComentRepository;
import com.asavin.hello.repository.ImageRepository;
import com.asavin.hello.repository.PostRepositpry;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepositpry postRepositpry;
    @Autowired
    ComentRepository comentRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    WebSocketService webSocketService;
    @Value("${imagesPath}")
    String imagesPath;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public void deletePost(Long id) {
        postRepositpry.deleteById(id);
    }

    @Override
    public void deleteComment(Long id) {
        comentRepository.deleteById(id);
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepositpry.findAll();
    }

    @Override
    public List<Coment> getAllComments() {
        return comentRepository.findAll();
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public List<Post> getPostWhereIdLessLimit(Long id, int quantity) {
        if (id != -1l && quantity != -1)
            return postRepositpry.getPostWhereIdLess(id, PageRequest.of(0, quantity));
        else if (id != -1l && quantity == -1)
            return postRepositpry.getPostWhereIdLess(id);
        else if (id == -1l && quantity != -1)
            return postRepositpry.findAllDesc(PageRequest.of(0, quantity));
        else
            return postRepositpry.findAllDesc();
    }

    @Override
    public List<Coment> getComentWhereIdLessLimit(Long id, int quantity) {
        if (id != -1l && quantity != -1)
            return comentRepository.getComentWhereIdLess(id, PageRequest.of(0, quantity));
        else if (id != -1l && quantity == -1)
            return comentRepository.getComentWhereIdLess(id);
        else if (id == -1l && quantity != -1)
            return comentRepository.findAllDesc(PageRequest.of(0, quantity));
        else
            return comentRepository.findAllDesc();
    }

    @Override
    public List<Image> getImageWhereIdLessLimit(Long id, int quantity) {
        if (id != -1l && quantity != -1)
            return imageRepository.getImageWhereIdLess(id, PageRequest.of(0, quantity));
        else if (id != -1l && quantity == -1)
            return imageRepository.getImageWhereIdLess(id);
        else if (id == -1l && quantity != -1)
            return imageRepository.findAllDesc(PageRequest.of(0, quantity));
        else
            return imageRepository.findAllDesc();
    }

    @Override
    public Coment writeComent(User user, String text, Post post) {
        Coment coment = new Coment();
        coment.setUser(user);
        coment.setPost(post);
        coment.setText(text);

        return comentRepository.saveAndFlush(coment);
    }

    @Override
    public Image saveImage(byte[] fileBytes, String type) {
        if(!type.equals("jpg") && !type.equals("png")){
            System.out.println("err "+type);
            return null;}
        Image image = imageRepository.save(new Image());
        image.setName(image.getId() + "." + type);
        image = imageRepository.save(image);
        File file = new File(imagesPath + "/" + image.getName());
        file.getParentFile().mkdirs();
        try  {
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            os.write(fileBytes);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BufferedImage squareImare(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            BufferedImage image = ImageIO.read(bais);
            int wight = image.getWidth();
            int heigth = image.getHeight();
            if(wight == heigth)
                return image;

            int minDim = Math.min(wight,heigth);
            int maxDim = Math.max(wight,heigth);
            return image.getSubimage((maxDim-minDim)/2,0,minDim,minDim);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isLiked(Post post, User user) {
        String sql = "SELECT COUNT(*) FROM likes where post_id=? and user_id=?";

        int count = jdbcTemplate.queryForObject(sql,new Object[]{post.getId(),user.getId()},Integer.class);
        return count!=0;
    }
}
