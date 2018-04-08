package com.babjo.whatdaybot.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class YesterdayCommand implements Command {

    private final Pattern pattern = Pattern.compile("어제 무슨 요일\\?");
    private final Clock clock;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        return new TextMessage(String.format("어제는 %s 입니다.", LocalDateTime.now(clock).plusDays(-1).getDayOfWeek()
                                                                         .name()));
    }
}
