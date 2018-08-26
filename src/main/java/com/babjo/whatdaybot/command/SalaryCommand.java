package com.babjo.whatdaybot.command;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

import com.babjo.whatdaybot.utils.SalaryDateUtils;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class SalaryCommand implements Command {

    private final Pattern pattern = Pattern.compile("(월급좀|월급\\?)");
    private final Clock clock;

    public SalaryCommand(Clock clock) {this.clock = clock;}

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        LocalDate date = LocalDate.now(clock);
        LocalTime time = LocalTime.now(clock);

        LocalDateTime now = LocalDateTime.of(date, time);
        LocalDateTime nextSalary = LocalDateTime.of(SalaryDateUtils.getNextSalaryDate(date), LocalTime.MIN);

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
