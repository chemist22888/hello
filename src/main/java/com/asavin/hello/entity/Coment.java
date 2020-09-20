package com.asavin.hello.entity;

import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@JsonView({UserViewJson.UserFullDetails.class})

public class Coment {

    Long id;

    public Coment() {
    }

    public Coment(User user, Post post, String text) {
        this.user = user;
        this.post = post;
        this.text = text;
    }
    @JsonIgnoreProperties({"friends","wall"})
    User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne
    @JoinColumn(name = "POST_ID",referencedColumnName = "id")
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    @Column
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    @JsonIgnore
    Post post;
    String text;
}
