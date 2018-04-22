package com.babjo.whatdaybot.command;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import com.babjo.whatdaybot.utils.LocalDateTimeUtils;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SalaryCommand implements Command {

    private final Pattern pattern = Pattern.compile("(월급좀|월급\\?)");
    private final Clock clock;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime nextSalary = LocalDateTimeUtils.getNextSalary(clock);

        System.out.println(now.toString());
        System.out.println(nextSalary.toString());

        Duration duration = Duration.between(now, nextSalary);
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long mins = duration.toMinutes();
        duration = duration.minusMinutes(mins);
        long secs = duration.toMillis() / 1000;

        return new TextMessage(String.format("다음 월급은 %d일 %d시간 %d분 %d초 남았습니다.", days, hours, mins, secs));
    }
}
