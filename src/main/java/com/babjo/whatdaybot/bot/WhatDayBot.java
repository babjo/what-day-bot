package com.babjo.whatdaybot.bot;

import com.babjo.whatdaybot.Room;
import com.babjo.whatdaybot.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WhatDayBot {

    private final RoomRepository roomRepository;

    public CommandResponse handle(StartCommand command) {
        roomRepository.save(new Room(command.getFrom(), true));
        return new CommandResponse("OK! START!");
    }

    public CommandResponse handle(StopCommand command) {
        roomRepository.save(new Room(command.getFrom(), false));
        return new CommandResponse("OK! STOP!");
    }
}
