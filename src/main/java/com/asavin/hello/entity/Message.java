package com.asavin.hello.entity;


import com.asavin.hello.json.ChatViewJson;
import com.asavin.hello.json.JPAEntityResolver;
import com.asavin.hello.json.MessageViewJson;
import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@JsonPropertyOrder("id")
@JsonView({UserViewJson.UserFullDetails.class, UserViewJson.UserInChatDetails.class})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Message {
    private long id;
    private User user;
    private String text;
    private Chat chat;

    public Message() {
    }

    public Message(String text, User user, Chat chat) {
        this.user = user;
        this.text = text;
        this.chat = chat;
    }

    @ManyToOne
    @JoinColumn(name = "CHAT_ID", referencedColumnName = "id")
    @JsonIgnoreProperties({"messages"})
    @JsonIdentityInfo(resolver = JPAEntityResolver.class, scope = Chat.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("chatId")
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
