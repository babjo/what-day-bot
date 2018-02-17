package com.babjo.whatdaybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

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
        return botService.handle(event.getSource().getSenderId(), event.getMessage().getText())
                         .map(TextMessage::new).orElse(null);
    }

    @Scheduled(cron = "0 30 09 ? * MON-FRI", zone = "Asia/Seoul")
    public void pushTodayOfWeekMessages() {
        botService.pushTodayOfWeekMessages();
    }

    @Scheduled(cron = "0 0 19 ? * MON-FRI", zone = "Asia/Seoul")
    public void pushOverworkQuestionMessages() {
        botService.pushWorkLateAtNightMessages();
    }
}
