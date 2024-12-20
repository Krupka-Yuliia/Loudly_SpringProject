package com.loudlyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LoudlyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoudlyAppApplication.class, args);
    }

}
