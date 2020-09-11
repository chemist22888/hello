package com.asavin.hello.entity;

import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
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


    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"friends","wall"})
    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    List<Image> images;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_image",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "image_id") }
    )
    @Column
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class,
            property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("images")
    @JsonView({UserViewJson.UserFullDetails.class})
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
@Column(name = "creation_date")
    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }


    private Instant creationDate;
    @ManyToOne
    @JoinColumn(name = "WALL_ID",referencedColumnName = "id")
  //  @Fetch(FetchMode.JOIN)
    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }


    @JsonIgnore
  //  @Fetch(FetchMode.JOIN)
    private Wall wall;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
