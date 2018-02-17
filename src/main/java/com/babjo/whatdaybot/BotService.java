package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotService {

    private final Clock clock;
    private final LineMessagingClient client;
    private final RoomRepository roomRepository;
    private final Random random;

    private final static Logger logger = LoggerFactory.getLogger(BotService.class);

    public Optional<String> handle(String from, String text) {
        String response = null;
        switch (text) {
            case "START":
                roomRepository.save(new Room(from, true));
                response = "OK! START!";
                break;
            case "STOP":
                roomRepository.save(new Room(from, false));
                response = "OK! STOP!";
                break;
            case "미워":
                response = "미워하지마";
                break;
            case "미웡":
                response = "미워하지마";
                break;
            case "어제 무슨 요일?":
                response = yesterday();
                break;
            case "금일 무슨 요일?":
                response = today();
                break;
            case "오늘 무슨 요일?":
                response = today();
                break;
            case "내일 무슨 요일?":
                response = tomorrow();
                break;
            case "모레 무슨 요일?":
                response = dayAfterTomorrow();
            default:
                break;
        }
        return Optional.ofNullable(response);
    }

    private String yesterday() {
        return String.format("어제는 %s 입니다.", now().plusDays(-1).getDayOfWeek().name());
    }

    private String today() {
        return String.format("오늘은 %s 입니다.", now().getDayOfWeek().name());
    }

    private String tomorrow() {
        return String.format("내일은 %s 입니다.", now().plusDays(1).getDayOfWeek().name());
    }

    private String dayAfterTomorrow() {
        return String.format("모레는 %s 입니다.", now().plusDays(2).getDayOfWeek().name());
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    public void pushTodayOfWeekMessages() {
        pushMessages(today());
    }

    public void pushWorkLateAtNightMessages() {
        pushMessages(workLateAtNight());
    }

    private String workLateAtNight() {
        String[] responses = new String[] { "오늘 야근?", "야근야근???", "오늘도 야근?!???", "야근 ㄱ?", "야근각?" };
        return responses[random.nextInt(responses.length)];
    }

    private void pushMessages(String text) {
        List<Room> rooms = roomRepository.findAll();
        rooms.stream().filter(Room::isBotRunning).forEach(room -> {
            final TextMessage textMessage = new TextMessage(text);
            final PushMessage pushMessage = new PushMessage(room.getId(), textMessage);
            try {
                client.pushMessage(pushMessage).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to push a message. roomId: {}", room.getId(), e);
            }
        });
    }
}
