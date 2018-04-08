package com.babjo.whatdaybot.command;

import java.util.regex.Pattern;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class HateYouCommand implements Command {

    private final Pattern pattern = Pattern.compile("(미워|미웡)");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        return new TextMessage("미워하지마");
    }
}
