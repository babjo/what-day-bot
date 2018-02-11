package com.babjo.whatdaybot.bot;

import static com.babjo.whatdaybot.bot.CommandResponse.NULL_RESPONSE;

import java.time.Clock;
import java.time.LocalDateTime;

import com.babjo.whatdaybot.Room;
import com.babjo.whatdaybot.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WhatDayBot {

    private final RoomRepository roomRepository;
    private final Clock clock;

    public CommandResponse handle(StartCommand command) {
        roomRepository.save(new Room(command.getFrom(), true));
        return new CommandResponse("OK! START!");
    }

    public CommandResponse handle(StopCommand command) {
        roomRepository.save(new Room(command.getFrom(), false));
        return new CommandResponse("OK! STOP!");
    }

    public CommandResponse handle(Command command) {
        Room room = roomRepository.findOne(command.getFrom());
        if (room == null) {
            return NULL_RESPONSE;
        }

        switch (command.getText()) {
            case "미워":
                return new CommandResponse("미워하지마");
            case "오늘 무슨 요일?":
                return new CommandResponse(String.format("오늘은 %s 입니다.", todayDay()));
            default:
                return NULL_RESPONSE;
        }
    }

    private String todayDay() {
        return LocalDateTime.now(clock).getDayOfWeek().name();
    }

}
