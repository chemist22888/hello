package com.asavin.hello.service;

import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import com.asavin.hello.entity.Post;
import com.asavin.hello.repository.ComentRepository;
import com.asavin.hello.repository.ImageRepository;
import com.asavin.hello.repository.PostRepositpry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepositpry postRepositpry;
    @Autowired
    ComentRepository comentRepository;
    @Autowired
    ImageRepository imageRepository;
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
            return imageRepository.findAllDesc();    }
}
