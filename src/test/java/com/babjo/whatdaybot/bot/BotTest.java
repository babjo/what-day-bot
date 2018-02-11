package com.babjo.whatdaybot.bot;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.then;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.babjo.whatdaybot.Room;
import com.babjo.whatdaybot.repository.RoomRepository;

public class BotTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private RoomRepository roomRepository;

    private WhatDayBot bot;

    @Before
    public void setUp() {
        bot = new WhatDayBot(roomRepository);
    }

    @Test
    public void handleStartCommand() {
        // GIVEN
        StartCommand startCommand = new StartCommand("A");

        // WHEN
        CommandResponse response = bot.handle(startCommand);

        // THEN
        assertEquals(response.getText(), "OK! START!");
        then(roomRepository).should().save(new Room("A", true));
    }

    @Test
    public void handleStopCommand() {
        // GIVEN
        StopCommand stopCommand = new StopCommand("A");

        // WHEN
        CommandResponse response = bot.handle(stopCommand);

        // THEN
        assertEquals(response.getText(), "OK! STOP!");
        then(roomRepository).should().save(new Room("A", false));
    }
}
