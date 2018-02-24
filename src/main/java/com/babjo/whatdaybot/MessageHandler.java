package com.babjo.whatdaybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
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
    public Message handleTextMessageEvent(MessageEvent<MessageContent> event) {
        logger.info("event: {}", event);
        if (event.getMessage() instanceof TextMessageContent) {
            TextMessageContent textMessageContent = (TextMessageContent) event.getMessage();
            return botService.handle(event.getSource().getSenderId(), textMessageContent.getText())
                             .orElse(null);
        }
        return null;
    }

    @Scheduled(cron = "0 30 09 ? * MON", zone = "Asia/Seoul")
    public void pushMondayMessage() {
        botService.pushMondayMessages();
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
