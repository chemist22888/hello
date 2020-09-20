package com.asavin.hello.entity;

import com.asavin.hello.json.EntityIdResolver;
import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
@JsonView({UserViewJson.UserFullDetails.class})
public class Post {
    private Long id;
    private String text;
    private List<Coment> coments;
    private List<Like> likes;
    private List<User> likers;

    @ManyToMany
    @JoinTable(name = "likes",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @JsonIgnoreProperties({"friends","wall"})
    public List<User> getLikers() {
        return likers;
    }

    public void setLikers(List<User> likers) {
        this.likers = likers;
    }

    public Post(Long id) {
        this.id = id;
    }
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})


    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"friends","wall"})
    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    List<Image> images;//TODO read about cascades
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_image",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "image_id") }
    )
    @Column//TODO jsonviews!!!!
    @JsonIdentityReference(alwaysAsId=true)
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Post() {
    }
    @OneToMany(mappedBy = "post")
    @JsonView({UserViewJson.UserFullDetails.class})
    public List<Coment> getComents() {
        return coments;
    }

    public void setComents(List<Coment> coments) {
        this.coments = coments;
    }
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})
    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }


    private Instant creationDate;
    @ManyToOne
    @JoinColumn(name = "WALL_ID",referencedColumnName = "id")
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})
    @JsonIgnoreProperties({"posts"})
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("wallId")
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
