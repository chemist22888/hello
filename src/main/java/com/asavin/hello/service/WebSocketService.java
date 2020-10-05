package com.asavin.hello.service;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Message;
import com.asavin.hello.entity.User;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;

@Service
public class WebSocketService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ChatService chatService;
    public void sendMessage(Long idFrom, Chat chat, Message message) {
        chat.getUsers().forEach(user -> {
            System.out.println(user.getUsername());
            try {
                this.messagingTemplate.convertAndSendToUser(user.getUsername(),"/queue/chat",
                        objectMapper.writerWithView(UserViewJson.UserInChatDetails.class).writeValueAsString(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
    public void sendFriendStatus(String userToUsername,User user ,int status) throws IOException {
        System.out.println("ws"+userToUsername);

        StringWriter sw = new StringWriter();
        objectMapper.writerWithView(UserViewJson.UserInChatDetails.class).writeValue(sw, user);

        JsonNode jsonNode = objectMapper.readTree(sw.toString());

        JsonNode response = objectMapper.createObjectNode()
        .put("friendStatus",status)
        .set("user",jsonNode);

        System.out.println(response);

        this.messagingTemplate.convertAndSendToUser(userToUsername,"/queue/friend",response);
    }
}
