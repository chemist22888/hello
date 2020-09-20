package com.asavin.hello.entity;

import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Image {
    public Image(Long id) {
        this.id = id;
    }

    Long id;

    @GeneratedValue
    @Column
    @Id
    @JsonValue
    @JsonView({UserViewJson.UserInChatDetails.class,UserViewJson.UserFullDetails.class})

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    String name;
    public Image(){}
}
