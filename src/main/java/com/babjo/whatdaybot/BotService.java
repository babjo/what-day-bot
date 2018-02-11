package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotService {

    private final RoomRepository roomRepository;
    private final Clock clock;
    private final LineMessagingClient client;
    private final static Logger logger = LoggerFactory.getLogger(BotService.class);

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
                return todayDay();
            default:
                return null;
        }
    }

    public String todayDay() {
        return String.format("오늘은 %s 입니다.", LocalDateTime.now(clock).getDayOfWeek().name());
    }

    public void pushMessages() {
        List<Room> rooms = roomRepository.findAll();
        rooms.stream().filter(Room::isBotRunning).forEach(room -> {
            final TextMessage textMessage = new TextMessage(todayDay());
            final PushMessage pushMessage = new PushMessage(room.getId(), textMessage);
            try {
                client.pushMessage(pushMessage).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to push a message. roomId: {}", room.getId(), e);
            }
        });
    }
}
