package com.asavin.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.SocketException;

@Service
public class GmailService implements EmailService {
    @Autowired
    JavaMailSender mailSender;

    @Override
    public void send(String subject, String text, String adress) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("gjhffjg1@gmail.com");//TODO configs
            message.setTo(adress);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            }catch (Exception e)
                {e.printStackTrace();}
    }
}
