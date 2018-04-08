package com.babjo.whatdaybot.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;

import org.junit.Test;

import com.babjo.whatdaybot.utils.EventUtils;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;

public class TodayCommandTest {

    @Test
    public void matches_WhenTextIs오늘무슨요일_ThenTrue() {
        // GIVEN
        Command command = new TodayCommand(Clock.systemUTC());
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("오늘 무슨 요일?");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void matches_WhenTextIs오늘무슨요일_ThenFalse() {
        // GIVEN
        Command command = new TodayCommand(Clock.systemUTC());
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("오늘 무슨 요일");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(false);
    }

}
