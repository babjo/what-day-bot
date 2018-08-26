package com.babjo.whatdaybot.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class TodayCommand implements Command {

    private final Pattern pattern = Pattern.compile("(오늘|금일) 무슨 요일\\?");
    private final Clock clock;

    public TodayCommand(Clock clock) {this.clock = clock;}

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        return new TextMessage(String.format("오늘은 %s 입니다.", LocalDateTime.now(clock).getDayOfWeek().name()));
    }
}
