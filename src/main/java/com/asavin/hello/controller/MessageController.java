package com.asavin.hello.controller;

import com.asavin.hello.entity.*;
import com.asavin.hello.json.ChatViewJson;
import com.asavin.hello.json.MessageViewJson;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.ChatRepository;
import com.asavin.hello.repository.MessageRepository;
import com.asavin.hello.repository.UserRepository;
import com.asavin.hello.service.ChatService;
import com.asavin.hello.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class MessageController {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserService userService;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    ChatService chatService;

    @GetMapping("/chats")
    @JsonView(MessageViewJson.MessageShortDetails.class)
    public List<Chat> chats() {
        return chatService.getAllMyChats();
    }

    @PostMapping("/send")
    @JsonView(MessageViewJson.MessageShortDetails.class)
    public Message send(@RequestParam Long id, @RequestParam String text) {
        return chatService.send(text,id);
    }

    @JsonView(MessageViewJson.MessageShortDetails.class)
    @GetMapping("chat/{id}")
//    @ResponseBody
    public Chat getChat(@PathVariable String id) {
       if(id.contains("c")) {
           Chat chat = chatService.getChatById(Long.parseLong(id.substring(1)));
           if(chat.getUsers().contains(userService.getMe()))
               return chat;
           else
               return null;
       }
       else
           return chatService.getDialogWithUser(Long.parseLong(id));
    }
    @PostMapping("/log")
    public void log( @RequestBody String body){
        System.out.println(body);
    }
}
//TODO заменить me на activeUser,просмотреть как работает одновременно для нескольких пользователей