package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotService {

    private final RoomRepository roomRepository;
    private final Clock clock;

    public String start(String from) {
        roomRepository.save(new Room(from, true));
        return "OK! START!";
    }

    public String stop(String from) {
        roomRepository.save(new Room(from, false));
        return "OK! STOP!";
    }

    public String handle(String from, String text) {
        Room room = roomRepository.findOne(from);
        if (room == null) {
            return null;
        }

        switch (text) {
            case "미워":
                return "미워하지마";
            case "오늘 무슨 요일?":
                return String.format("오늘은 %s 입니다.", todayDay());
            default:
                return null;
        }
    }

    private String todayDay() {
        return LocalDateTime.now(clock).getDayOfWeek().name();
    }

}
