package com.babjo.whatdaybot.utils;

import java.time.Instant;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;

public class EventUtils {

    public static String getText(MessageEvent<MessageContent> event) {
        if (!(event.getMessage() instanceof TextMessageContent)) {
            return null;
        }

        return ((TextMessageContent) event.getMessage()).getText();
    }

    public static MessageEvent<MessageContent> createTextMessage(String text) {
        String replyToken = "replyToken";
        Source source = new RoomSource("userId", "roomId");
        TextMessageContent message = new TextMessageContent("id", text);
        Instant timestamp = Instant.EPOCH;
        return new MessageEvent<>(replyToken, source, message, timestamp);
    }
}
