package com.asavin.hello.entity;

import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@JsonView({UserViewJson.UserFullDetails.class})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Post {
    private Long id;
    private String text;
    private Set<Coment> coments;
    private Set<Like> likes;
    private Set<User> likers;
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @JsonIgnore
    @JsonIgnoreProperties({"wall","friends","posts"})
    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToMany
    @JoinTable(name = "likes",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @JsonIgnoreProperties({"friends","wall","posts"})
    @ElementCollection
    public Set<User> getLikers() {
        return likers;
    }

    public void setLikers(Set<User> likers) {
        this.likers = likers;
    }

    public Post(Long id) {
        this.id = id;
    }
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})


    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"friends","wall"})
    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    Set<Image> images;//TODO read about cascades
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_image",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "image_id") }
    )
    @Column//TODO jsonviews!!!!
    @JsonIdentityReference(alwaysAsId=true)
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})
    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Post() {
    }
    @OneToMany(mappedBy = "post")
    @JsonView({UserViewJson.UserFullDetails.class})
    public Set<Coment> getComents() {
        return coments;
    }

    public void setComents(Set<Coment> coments) {
        this.coments = coments;
    }
    @CreatedDate
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})
    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }


    private Instant creationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WALL_ID",referencedColumnName = "id")
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})
    @JsonIgnoreProperties({"posts"})
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("wallId")
    @JsonIgnore
    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }


    @JsonIgnore
    private Wall wall;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
