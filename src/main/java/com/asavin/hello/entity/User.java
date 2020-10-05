package com.asavin.hello.entity;

import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedEntityGraph(name = "graph.Full",attributeNodes = {
        @NamedAttributeNode(value = "avatar"),
        @NamedAttributeNode(value = "posts",subgraph = "postsubs"),
        @NamedAttributeNode(value = "friends"),
    },subgraphs = {
        @NamedSubgraph(name = "postsubs",attributeNodes =
                {@NamedAttributeNode(value = "likes"),
                 @NamedAttributeNode(value = "coments"),
                 @NamedAttributeNode(value = "likers"),
                 @NamedAttributeNode(value = "images"),
                        })})
public class User implements UserDetails {
    @OneToMany(mappedBy = "user")
    @JsonView({UserViewJson.UserFullDetails.class})
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> getPosts) {
        this.posts = getPosts;
    }

    Set<Post> posts;
    @Column
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;
    public User(Long id) {
        this.id = id;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar",referencedColumnName = "id")
    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }


    private Image avatar;
    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")})
    @JsonView(UserViewJson.UserFullDetails.class)
    @JsonIgnoreProperties({"friends", "wall"})

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
    private Long id;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    private String username;

    private String password;

    private List<Chat> chats;

    private Wall wall;
    private String email;
//
//    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
//    @JsonIgnore
//    public UserLastActivity getLastActivity() {
//        return lastActivity;
//    }
//
//    public void setLastActivity(UserLastActivity lastActivity) {
//        this.lastActivity = lastActivity;
//    }
//
//
//    private UserLastActivity lastActivity;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    private Set<User> friends;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wall_id")
    @JsonIgnore
    @JsonView({UserViewJson.UserFullDetails.class})
    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "USER_CHAT",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    @OrderBy(value = "id asc")
    @JsonIgnore
    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UserViewJson.UserFullDetails.class, UserViewJson.UserInChatDetails.class})
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    @JsonView({UserViewJson.UserFullDetails.class, UserViewJson.UserInChatDetails.class})
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role);
    }
    @Column
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
