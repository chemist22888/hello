package com.asavin.hello.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity(name = "likes")
public class Like {
    private long id;
    private User user;
    private Post post;
    public Like( User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JsonIgnoreProperties({"friends","wall"})
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public Like() {
    }

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
