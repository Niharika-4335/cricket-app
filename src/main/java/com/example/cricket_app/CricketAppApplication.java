package com.example.cricket_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableScheduling
@EnableWebMvc
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CricketAppApplication {


    public static void main(String[] args) {
        SpringApplication.run(CricketAppApplication.class, args);
    }

}
