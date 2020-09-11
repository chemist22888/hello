package com.asavin.hello.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class UserLastActivity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    public UserLastActivity() {
    }

    public UserLastActivity(Long id, User user, Instant lastActivityTime) {
        this.id = id;
        this.user = user;
        this.lastActivityTime = lastActivityTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(Instant lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    private Instant lastActivityTime;
}
