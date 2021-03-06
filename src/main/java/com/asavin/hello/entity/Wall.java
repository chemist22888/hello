package com.asavin.hello.entity;


import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@JsonView({UserViewJson.UserFullDetails.class})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Wall {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "wall")
    @OrderBy("id asc")
    @JsonView({UserViewJson.UserFullDetails.class})
    private Set<Post> posts;

    @OneToOne(mappedBy = "wall", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private User user;

    @ManyToMany

    public Set<Post> getPosts() {
        return posts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
