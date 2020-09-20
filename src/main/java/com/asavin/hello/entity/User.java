package com.asavin.hello.entity;

import com.asavin.hello.json.ChatViewJson;
import com.asavin.hello.json.MessageViewJson;
import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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

public class User implements UserDetails {
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

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    @ManyToOne
    @JoinColumn(name = "avatar",referencedColumnName = "id")
    private Image avatar;

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UserViewJson.UserFullDetails.class, UserViewJson.UserInChatDetails.class})
    private Long id;


    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    @Column
    @JsonView({UserViewJson.UserFullDetails.class, UserViewJson.UserInChatDetails.class})
    private String username;
    @Column
    @JsonIgnore
    private String password;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "USER_CHAT",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    @OrderBy(value = "id asc")
    @JsonIgnore
    private List<Chat> chats;

    @OneToOne
    @JoinColumn(name = "wall_id")
    @JsonView({UserViewJson.UserFullDetails.class})
    private Wall wall;
    private String email;

    @JsonIgnore
    public UserLastActivity getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(UserLastActivity lastActivity) {
        this.lastActivity = lastActivity;
    }

    @OneToOne(mappedBy = "user")
    private UserLastActivity lastActivity;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    @JsonView(UserViewJson.UserFullDetails.class)
    @JsonIgnoreProperties({"friends", "wall"})
    private Set<User> friends;

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore

    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role);
    }

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
