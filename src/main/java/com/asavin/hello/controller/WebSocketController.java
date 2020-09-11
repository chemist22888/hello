package com.asavin.hello.controller;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.FriendRequest;
import com.asavin.hello.entity.Message;
import com.asavin.hello.entity.User;
import com.asavin.hello.json.ChatViewJson;
import com.asavin.hello.json.MessageViewJson;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.service.ChatService;
import com.asavin.hello.service.UserService;
import com.asavin.hello.service.WebSocketService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.concurrent.ExecutionException;

@Controller
@CrossOrigin(origins = "*")
public class WebSocketController {
    @Autowired
    ChatService chatService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    UserService userService;
    @Autowired
    WebSocketService webSocketService;
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @JsonView(MessageViewJson.MessageShortDetails.class)
    @MessageMapping("/send/message/{chatId}")
    @SendToUser("/chat")
    public Message chat(@DestinationVariable String chatId, @RequestBody Message message, Principal principal) throws Exception {
        Chat chat = null;
        if(chatId.toCharArray()[0] == 'c'){
            try {
                chat = chatService.getChatById(Long.parseLong(chatId.substring(1)));
            }catch (Exception e){e.printStackTrace();}
        }
        else {
            try {
                chat = chatService.getDialogWithUser(Long.parseLong(chatId));
            }catch (Exception e){e.printStackTrace();}
        }
        String me = "indef";

        try {
            me = principal.getName();
        }catch (Exception e){e.printStackTrace();}

        message = chatService.send(message.getText(),chat.getId());

        System.out.println(message.getText());
//        message.setChat(null);
//        webSocketService.sendMessage(1l,chatService.getChatById(Long.valueOf(1)),me+","+message);
        return message;
//        this.messagingTemplate.convertAndSend("/chat/"+chatId,message+"resended");
    }

    @JsonView(MessageViewJson.MessageShortDetails.class)
    @MessageMapping("/friend")
//    @SendToUser("/chat")
    public void request(@RequestParam("operation") int operation, Long id) throws Exception {
        User me = userService.getMe();
        User other = userService.findUserById(id);

        switch (operation){
            case FriendRequest.ACCEPT_FRIEND_REQUEST:
                userService.acceptFriendRequest(me,other);
                break;
            case FriendRequest.APPLY_FRIEND_REQUEST:
                userService.applyFriendRequest(me,other);
                break;
            case FriendRequest.CANCEL_FRIEND_REQUEST:
                userService.cancelFriendRequest(me,other);
                break;
            case FriendRequest.DELETE_FRIEND:
                userService.unbound(me,other);
                break;

        }
        webSocketService.sendFriendStatus(other.getUsername(),other,operation);

//
//        Chat chat = null;
//        if(chatId.toCharArray()[0] == 'c'){
//            try {
//                chat = chatService.getChatById(Long.parseLong(chatId.substring(1)));
//            }catch (Exception e){e.printStackTrace();}
//        }
//        else {
//            try {
//                chat = chatService.getDialogWithUser(Long.parseLong(chatId));
//            }catch (Exception e){e.printStackTrace();}
//        }
//        String me = "indef";
//
//        try {
//            me = principal.getName();
//        }catch (Exception e){e.printStackTrace();}
//
//        message = chatService.send(message.getText(),chat.getId());
//
//        System.out.println(message.getText());
//        message.setChat(null);
//        webSocketService.sendMessage(1l,chatService.getChatById(Long.valueOf(1)),me+","+message);
//        this.messagingTemplate.convertAndSend("/chat/"+chatId,message+"resended");
    }
}