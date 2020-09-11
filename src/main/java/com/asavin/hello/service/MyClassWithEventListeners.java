package com.asavin.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class MyClassWithEventListeners {
    @Autowired
    PasswordEncoder passwordEncoder;
    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
        System.out.println(passwordEncoder.encode("password"));
    }
}