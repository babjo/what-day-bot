package com.babjo.whatdaybot.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.babjo.whatdaybot.utils.EventUtils;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;

public class HateYouCommandTest {

    @Test
    public void matches_WhenTextIs미워_ThenFalse() {
        // GIVEN
        Command command = new HateYouCommand();
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("미워요");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void matches_WhenTextIs미워_ThenTrue() {
        // GIVEN
        Command command = new HateYouCommand();
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("미워");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void matches_WhenTextIs미웡_ThenTrue() {
        // GIVEN
        Command command = new HateYouCommand();
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("미웡");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(true);
    }
}
