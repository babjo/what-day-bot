package com.babjo.whatdaybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.babjo.whatdaybot.service.BotService;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class MessageHandler {

    private final BotService botService;
    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    public MessageHandler(BotService botService) {this.botService = botService;}

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
