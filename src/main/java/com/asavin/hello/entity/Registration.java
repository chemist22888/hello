package com.asavin.hello.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.id.UUIDGenerationStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Registration {
    private UUID uuid;
    private String email;
    private String username;
    private String password;
    private Instant registrationTime;

    public Registration() {
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDBinaryType")
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "registration_time")
    public Instant getRegistrationTime() {
        return registrationTime;
    }

    public Registration(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public void setRegistrationTime(Instant registrationTime) {
        this.registrationTime = registrationTime;
    }
}
