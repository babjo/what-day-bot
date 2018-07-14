package com.babjo.whatdaybot.storage;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babjo.whatdaybot.command.CommandExecutor;
import com.babjo.whatdaybot.model.Room;
import com.babjo.whatdaybot.repository.RoomRepository;
import com.babjo.whatdaybot.utils.EventUtils;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotService {

    private final LineMessagingClient client;
    private final RoomRepository roomRepository;
    private final CommandExecutor commandExecutor;
    private final Random random;

    private final static Logger logger = LoggerFactory.getLogger(BotService.class);

    public Message handleEvent(MessageEvent<MessageContent> event) {
        return commandExecutor.execute(event);
    }

    public void pushTodayOfWeekMessage() {
        pushMessage(commandExecutor.execute(EventUtils.createTextMessage("오늘 무슨 요일?")));
    }

    public void pushWorkLateAtNightMessage() {
        String[] responses = new String[] { "오늘 야근?", "야근야근???", "오늘도 야근?!???", "야근 ㄱ?", "야근각?" };
        pushMessage(new TextMessage(responses[random.nextInt(responses.length)]));
    }

    public void pushMondayMessage() {
        pushMessage(commandExecutor.execute(EventUtils.createTextMessage("월요송")));
    }

    private void pushMessage(Message message) {
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
