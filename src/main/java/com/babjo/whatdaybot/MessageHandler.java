package com.babjo.whatdaybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class MessageHandler {

    private final BotService botService;
    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    public MessageHandler(BotService botService) {this.botService = botService;}

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        logger.info("event: {}", event);
        return new TextMessage(handle(event));
    }

    private String handle(MessageEvent<TextMessageContent> event) {
        switch (event.getMessage().getText().toUpperCase()) {
            case "START":
                return botService.start(event.getSource().getSenderId());
            case "STOP":
                return botService.stop(event.getSource().getSenderId());
            default:
                return botService.handle(event.getSource().getSenderId(), event.getMessage().getText());
        }
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        logger.info("event: {}", event);
    }

    @Scheduled(cron = "0 0 10 ? * MON-FRI", zone = "Asia/Seoul")
    public void pushTodayOfWeekMessages() {
        botService.pushTodayOfWeekMessages();
    }

    @Scheduled(cron = "0 0 19 ? * MON-FRI", zone = "Asia/Seoul")
    public void pushOverworkQuestionMessages() {
        botService.pushWorkLateAtNightMessages();
    }
}
