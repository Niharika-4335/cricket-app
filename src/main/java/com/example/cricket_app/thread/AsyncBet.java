package com.example.cricket_app.thread;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncBet {
    @Async
    public void processBetData(Long userId) {
        try {
            Thread.sleep(10000);
            System.out.println("Processing data:" + userId + " in thread: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
