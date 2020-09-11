package com.asavin.hello.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OnlineStatusChangeNotifier {
    @Scheduled(fixedRate = 5000)
    public void checkStatusChange() {

    }
}
