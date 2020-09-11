package com.asavin.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Base64;
import java.util.Map;

//@Component
public class BullShit implements WebSocketMessageBrokerConfigurer {
    @Autowired
    AuthenticationManager authenticationManager;
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

//                    message.
                    String token = (((Map)message.getHeaders().get("nativeHeaders")).get("Authorization")).toString();
                    token = token.substring(7);
                    token = token.substring(0,token.length()-1);

                    token = new String(Base64.getDecoder().decode(token.getBytes()));
                    System.out.println(token);

                    String username = token.substring(0,token.indexOf(':'));
                    String password = token.substring(token.indexOf(':')+1);

                    Authentication auth = new UsernamePasswordAuthenticationToken(username,password);
                    System.out.println(username);
                    System.out.println(password);
//                    Authentication user = .
//                    .. ; // access authentication header(s)
                    accessor.setUser(auth);
                }
                return message;
            }
        });
    }
}