package com.babjo.whatdaybot.command;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.List;
import java.util.regex.Pattern;

import com.babjo.whatdaybot.model.Room;
import com.babjo.whatdaybot.repository.RoomRepository;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class AllRoomStateCommand implements Command {

    private final Pattern pattern = Pattern.compile("RoomState", Pattern.CASE_INSENSITIVE);
    private final RoomRepository roomRepository;

    public AllRoomStateCommand(RoomRepository roomRepository) {this.roomRepository = roomRepository;}

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        List<Room> room = roomRepository.findAll();
        return new TextMessage(
                String.join("\n", room.stream().map(r -> String
                        .format("RoomId: %s, isRunning: %b", r.getId(), r.isBotRunning()))
                                      .collect(toImmutableList())));
    }
}
