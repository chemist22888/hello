package com.asavin.hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.springframework.messaging.simp.SimpMessageType.*;

@Configuration
public class SocketSecurityConfig 
  extends AbstractSecurityWebSocketMessageBrokerConfigurer {


    @Override
    protected void configureInbound(
            MessageSecurityMetadataSourceRegistry messages) {
        messages.simpTypeMatchers(CONNECT, UNSUBSCRIBE, DISCONNECT, HEARTBEAT)
                .permitAll();
//                .anyMessage().authenticated();
//                .simpTypeMatchers(CONNECT, UNSUBSCRIBE, DISCONNECT).permitAll()
//                .simpSubscribeDestMatchers("/**").authenticated();
    }
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}