package com.babjo.whatdaybot.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.Mock;

import com.babjo.whatdaybot.repository.RoomRepository;
import com.babjo.whatdaybot.utils.EventUtils;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;

public class StartCommandTest {

    @Mock
    private RoomRepository roomRepository;

    @Test
    public void matches_WhenTextIsStart_ThenTrue() {
        // GIVEN
        Command command = new StartCommand(roomRepository);
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("Start");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void matches_WhenTextIsStarT_ThenTrue() {
        // GIVEN
        Command command = new StartCommand(roomRepository);
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("StarT");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void matches_WhenTextIsSTART_ThenTrue() {
        // GIVEN
        Command command = new StartCommand(roomRepository);
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("START");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void matches_WhenTextIsStop_ThenFalse() {
        // GIVEN
        Command command = new StartCommand(roomRepository);
        MessageEvent<MessageContent> event = EventUtils.createTextMessage("Stop");

        // WHEN
        boolean result = command.matches(event);

        // THEN
        assertThat(result).isEqualTo(false);
    }

}
