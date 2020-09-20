package com.asavin.hello.entity;


import com.asavin.hello.json.ChatViewJson;
import com.asavin.hello.json.MessageViewJson;
import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Chat {
    @JsonCreator
    public Chat(@JsonProperty("id") long id) {
        this.id = id;
    }

    private long id;
    private List<User> users;

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "USER_CHAT",
            joinColumns = {@JoinColumn(name = "chat_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @JsonView({UserViewJson.UserFullDetails.class, UserViewJson.UserInChatDetails.class})

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "chat", orphanRemoval = true)
    @JsonView({MessageViewJson.MessageShortDetails.class})
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> message) {
        this.messages = message;
    }

    private List<Message> messages;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UserViewJson.UserFullDetails.class, UserViewJson.UserInChatDetails.class})
    public long getId() {
        return id;
    }

    public Chat(List<User> users, String name, List<Message> messages) {
        this.users = users;
        this.name = name;
        this.messages = messages;
    }

    public Chat() {
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

}
