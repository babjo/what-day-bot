package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.VideoMessage;

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
                    return new TextMessage("OK! START!");
                }),
                new Command("STOP", "지긋지긋한 메세지 스케줄러 그만", from -> {
                    roomRepository.save(new Room(from, false));
                    return new TextMessage("OK! STOP!");
                }),
                new Command("미워", "-", ignored -> new TextMessage("미워하지마")),
                new Command("미웡", "-", ignored -> new TextMessage("미워하지마")),
                new Command("어제 무슨 요일?", "-", ignored -> yesterdayTextMessage()),
                new Command("금일 무슨 요일?", "-", ignored -> todayTextMessage()),
                new Command("오늘 무슨 요일?", "-", ignored -> todayTextMessage()),
                new Command("내일 무슨 요일?", "-", ignored -> tomorrowTextMessage()),
                new Command("모레 무슨 요일?", "-", ignored -> dayAfterTomorrowTextMessage()),
                new Command("월요송", "-", ignored -> mondaySongVideoMessage())
        );
    }

    public Message handleTextMessage(String from, TextMessageContent textMessageContent) {
        String upperText = textMessageContent.getText().toUpperCase();
        if ("HELP".equals(upperText)) {
            return helpTextMessage();
        }
        for (Command command : commands) {
            if (command.getName().equals(upperText)) {
                return command.getFunction().apply(from);
            }
        }
        return null;
    }

    private TextMessage helpTextMessage() {
        return new TextMessage(commands.stream()
                                       .map(command -> String.format("\nCommand: %s\nDescription: %s",
                                                                     command.getName(),
                                                                     command.getDescription()))
                                       .reduce("Command list", String::concat));
    }

    private TextMessage yesterdayTextMessage() {
        return new TextMessage(String.format("어제는 %s 입니다.", now().plusDays(-1).getDayOfWeek().name()));
    }

    private TextMessage todayTextMessage() {
        return new TextMessage(String.format("오늘은 %s 입니다.", now().getDayOfWeek().name()));
    }

    private TextMessage tomorrowTextMessage() {
        return new TextMessage(String.format("내일은 %s 입니다.", now().plusDays(1).getDayOfWeek().name()));
    }

    private TextMessage dayAfterTomorrowTextMessage() {
        return new TextMessage(String.format("모레는 %s 입니다.", now().plusDays(2).getDayOfWeek().name()));
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    public void pushTodayOfWeekMessages() {
        pushMessages(todayTextMessage());
    }

    public void pushWorkLateAtNightMessages() {
        pushMessages(workLateAtNight());
    }

    public void pushMondayMessages() {
        pushMessages(mondaySongVideoMessage());
    }

    private VideoMessage mondaySongVideoMessage() {
        return new VideoMessage("https://vt.media.tumblr.com/tumblr_p4nukupwiG1x76q2h.mp4",
                                "https://78.media.tumblr.com/3677c5e4ba151cc86d19999e0d2e8855/tumblr_p4nuzyVKwJ1x76q2ho1_r1_1280.png");
    }

    private TextMessage workLateAtNight() {
        String[] responses = new String[] { "오늘 야근?", "야근야근???", "오늘도 야근?!???", "야근 ㄱ?", "야근각?" };
        return new TextMessage(responses[random.nextInt(responses.length)]);
    }

    private void pushMessages(Message message) {
        List<Room> rooms = roomRepository.findAll();
        rooms.stream().filter(Room::isBotRunning).forEach(room -> {
            final PushMessage pushMessage = new PushMessage(room.getId(), message);
            try {
                client.pushMessage(pushMessage).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to push a message. roomId: {}", room.getId(), e);
            }
        });
    }
}
