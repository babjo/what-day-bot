package com.babjo.whatdaybot.command;

import java.util.regex.Pattern;

import com.babjo.whatdaybot.utils.EventUtils;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;

public interface Command {

    default boolean matches(MessageEvent<MessageContent> event) {
        String text = EventUtils.getText(event);
        if (text == null) {
            return false;
        }

        return getPattern().matcher(text).matches();
    }

    Pattern getPattern();

    Message execute(MessageEvent<MessageContent> event);
}
