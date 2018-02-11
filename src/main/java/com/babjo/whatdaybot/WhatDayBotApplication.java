package com.babjo.whatdaybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import com.babjo.whatdaybot.bot.Command;
import com.babjo.whatdaybot.bot.StartCommand;
import com.babjo.whatdaybot.bot.StopCommand;
import com.babjo.whatdaybot.bot.WhatDayBot;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class WhatDayBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(WhatDayBotApplication.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(WhatDayBotApplication.class);

    @Autowired
    private WhatDayBot whatDayBot;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        logger.info("event: {}", event);
        return new TextMessage(handle(event));
    }

    private String handle(MessageEvent<TextMessageContent> event) {
        switch (event.getMessage().getText().toUpperCase()) {
            case "START":
                return whatDayBot.handle(new StartCommand(event.getSource().getSenderId())).getText();
            case "STOP":
                return whatDayBot.handle(new StopCommand(event.getSource().getSenderId())).getText();
            default:
                return whatDayBot.handle(
                        new Command(event.getSource().getSenderId(), event.getMessage().getText())).getText();
        }
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        logger.info("event: {}", event);
    }

    @Scheduled(cron = "* * * * MON-SUN")
    public void pushMessages() {
        logger.info("pushMessages");
    }
}
