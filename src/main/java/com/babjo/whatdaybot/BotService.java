package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

public class BotService {

    private final Clock clock;
    private final LineMessagingClient client;
    private final RoomRepository roomRepository;
    private final Random random;
    private List<Command> commands;

    private final static Logger logger = LoggerFactory.getLogger(BotService.class);

    public BotService(Clock clock, LineMessagingClient client, RoomRepository roomRepository,
                      Random random) {
        this.clock = clock;
        this.client = client;
        this.roomRepository = roomRepository;
        this.random = random;

        initCommands();
    }

    private void initCommands() {
        commands = ImmutableList.of(
                new Command("START", "메세지 스케줄러 시작", from -> {
                    roomRepository.save(new Room(from, true));
                    return "OK! START!";
                }),
                new Command("STOP", "지긋지긋한 메세지 스케줄러 그만", from -> {
                    roomRepository.save(new Room(from, false));
                    return "OK! STOP!";
                }),
                new Command("미워", "-", ignored -> "미워하지마"),
                new Command("미웡", "-", ignored -> "미워하지마"),
                new Command("어제 무슨 요일?", "-", ignored -> yesterday()),
                new Command("금일 무슨 요일?", "-", ignored -> today()),
                new Command("오늘 무슨 요일?", "-", ignored -> today()),
                new Command("내일 무슨 요일?", "-", ignored -> tomorrow()),
                new Command("모레 무슨 요일?", "-", ignored -> dayAfterTomorrow())
        );
    }

    public Optional<String> handle(String from, String text) {
        String upperText = text.toUpperCase();
        if ("HELP".equals(upperText)) {
            return Optional.of(help());
        }
        for (Command command : commands) {
            if (command.getName().equals(upperText)) {
                return Optional.of(command.getFunction().apply(from));
            }
        }
        return Optional.empty();
    }

    private String help() {
        return commands.stream()
                       .map(command -> String.format("\nCommand: %s\nDescription: %s",
                                                     command.getName(),
                                                     command.getDescription()))
                       .reduce("Command list", String::concat);
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
