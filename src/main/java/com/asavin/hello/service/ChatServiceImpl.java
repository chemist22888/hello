package com.asavin.hello.service;

import com.asavin.hello.entity.*;
import com.asavin.hello.repository.ChatRepository;
import com.asavin.hello.repository.MessageRepository;
import com.asavin.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ChatServiceImpl implements ChatService{
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Chat getChatById(Long id) {
        return chatRepository.findById(id).get();//getMe().getChats().get(toIntExact(id));
    }

    @Override
    public Chat getDialogWithUser(Long userId) {
        User user = userRepository.findById(userId).get();
        Chat dialog = chatRepository.findDilog(getMe(),user);
        if(dialog == null){
            dialog = new Chat();
            dialog.setUsers(Arrays.asList(getMe(), user));
            dialog.setMessages(new ArrayList());
            dialog = chatRepository.save(dialog);
        }
        return dialog;
    }

    @Override
    public Chat createChat(List<User> users) {
        Chat chat = new Chat();
        chat.setUsers(users);
        chat = chatRepository.save(chat);
        return chat;
    }

    @Override
    public Message send(String text, Long chatId) {
        Message message = new Message();
        message.setText(text);
        message.setChat(chatRepository.findById(chatId).get());
        message.setUser(getMe());
        return messageRepository.save(message);
    }

    @Override
    public List<Chat> getAllMyChats() {
        return getMe().getChats();
    }

    @Override
    public boolean isChatBetween(User user1, User user2) {
        Chat dialog = chatRepository.findDilog(user1,user2);
        return dialog != null;
    }

    private User getMe(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }
}
