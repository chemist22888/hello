package com.asavin.hello.service;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.User;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.ObjectNode;
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
    public void sendMessage(Long idFrom, Chat chat, String text){
        this.messagingTemplate.convertAndSend("/chat/"+chat.getId(),text+"resended");
    }
    public void sendFriendStatus(String userToUsername,Long id ,int status) throws JsonProcessingException {
        System.out.println("ws"+userToUsername);
        User user = userRepository.findById(id).get();

        String response = objectMapper.createObjectNode()
        .put("friendStatus",status)
        .put("user",objectMapper.writerWithView(UserViewJson.UserInChatDetails.class).writeValueAsString(user)).toString();
        this.messagingTemplate.convertAndSendToUser(userToUsername,"/queue/friend",response);

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
    public void sendFriendStatus(String userToUsername,String username  ,int status){
        String response = objectMapper.createObjectNode()
                .put("friendStatus",status)
                .put("username",username).toString();

        this.messagingTemplate.convertAndSendToUser(userToUsername,"user/friend",response);

    }
}
