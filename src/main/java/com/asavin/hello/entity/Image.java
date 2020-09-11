package com.asavin.hello.entity;

import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Image {
    @GeneratedValue
    @Column
    @Id
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Post> getPosts() {
//        return posts;
//    }
//
//    public void setPosts(List<Post> posts) {
//        this.posts = posts;
//    }

    @Column
    @JsonValue
    String name;
//    @JsonIgnore
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @Column
//    @JoinTable(
//            name = "POST_IMAGE",
//            joinColumns = { @JoinColumn(name = "image_Id") },
//            inverseJoinColumns = { @JoinColumn(name = "post_id") }
//    )
//    List<Post> posts;
}
