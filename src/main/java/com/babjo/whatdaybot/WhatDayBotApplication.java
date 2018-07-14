package com.babjo.whatdaybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babjo.whatdaybot.storage.BotService;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@RestController
@LineMessageHandler
public class WhatDayBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(WhatDayBotApplication.class, args);
    }

    @Autowired
    private BotService botService;
    private final static Logger logger = LoggerFactory.getLogger(WhatDayBotApplication.class);

    @GetMapping(value = "/health")
    public String checkHealthy() {
        return "OK";
    }

    @EventMapping
    public Message handleEvent(MessageEvent<MessageContent> event) {
        logger.info("event: {}", event);
        return botService.handleEvent(event);
    }

    @Scheduled(cron = "0 30 09 ? * MON", zone = "Asia/Seoul")
    public void pushMondayMessage() {
        botService.pushMondayMessage();
    }

    @Scheduled(cron = "0 30 09 ? * MON-FRI", zone = "Asia/Seoul")
    public void pushTodayOfWeekMessages() {
        botService.pushTodayOfWeekMessage();
    }

    @Scheduled(cron = "0 0 19 ? * MON-FRI", zone = "Asia/Seoul")
    public void pushOverworkQuestionMessages() {
        botService.pushWorkLateAtNightMessage();
    }
}
