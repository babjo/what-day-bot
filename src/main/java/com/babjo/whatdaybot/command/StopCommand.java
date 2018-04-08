package com.babjo.whatdaybot.command;

import java.util.regex.Pattern;

import com.babjo.whatdaybot.model.Room;
import com.babjo.whatdaybot.repository.RoomRepository;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StopCommand implements Command {

    private final Pattern pattern = Pattern.compile("STOP", Pattern.CASE_INSENSITIVE);
    private final RoomRepository roomRepository;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        roomRepository.save(new Room(event.getSource().getSenderId(), false));
        return new TextMessage("OK! STOP!");
    }
}
