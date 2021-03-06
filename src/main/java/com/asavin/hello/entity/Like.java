package com.asavin.hello.entity;


import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity(name = "likes")
@Table(name="likes",
        uniqueConstraints = {@UniqueConstraint(columnNames={"user_id", "post_id"})})
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
    @JsonIgnoreProperties({"friends","wall","posts"})
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public Like() {}

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonIgnoreProperties({"likes","comments","likers"})
    @JsonProperty("postId")
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
