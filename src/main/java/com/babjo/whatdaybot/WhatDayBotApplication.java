package com.babjo.whatdaybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WhatDayBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(WhatDayBotApplication.class, args);
    }

    @GetMapping(value = "/health")
    public String checkHealthy() {
        return "OK";
    }
}
