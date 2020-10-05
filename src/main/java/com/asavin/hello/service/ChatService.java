package com.asavin.hello.service;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.Message;
import com.asavin.hello.entity.User;

import java.util.List;
import java.util.Set;

public interface ChatService {
    Chat getChatById(Long id);
    Chat getDialogWithUser(Long userId);
    Chat createChat(Set<User> users);
    Message send(String text,Long chatId);
    List<Chat>getAllMyChats();
    boolean isChatBetween(User user1,User user2);
}
