package com.asavin.hello.entity;


import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonView({UserViewJson.UserFullDetails.class})

public class Wall {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "wall")
    @OrderBy("id asc")
    @JsonView({UserViewJson.UserFullDetails.class})
  //  @Fetch(FetchMode.JOIN)
    private List<Post>posts;

    @OneToOne(mappedBy = "wall", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
  //  @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private User user;

    @ManyToMany

    public List<Post> getPosts() {
        return posts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
