package com.asavin.hello.entity;

import javax.persistence.*;

@Entity
public class FriendRequest {
    public static final int APPLY_FRIEND_REQUEST = 1;
    public static final int CANCEL_FRIEND_REQUEST = -1;
    public static final int  ACCEPT_FRIEND_REQUEST = 2;
    public static final int DELETE_FRIEND = -2;
    public static final int DECLINE_FRIEND_REQUEST = 0;


    private Long id;
    private User from;
    private User to;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public FriendRequest() {
    }

    public FriendRequest(User from, User to) {
        this.from = from;
        this.to = to;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id",referencedColumnName = "id")
    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id",referencedColumnName = "id")
    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
}
