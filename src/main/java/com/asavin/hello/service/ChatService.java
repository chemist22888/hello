package com.asavin.hello.service;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.Message;
import com.asavin.hello.entity.User;

import java.util.List;

public interface ChatService {
    Chat getChatById(Long id);
    Chat getDialogWithUser(Long userId);
    Chat createChat(List<User> users);
    Message send(String text,Long chatId);
    List<Chat>getAllMyChats();
    boolean isChatBetween(User user1,User user2);
}
