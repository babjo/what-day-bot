package com.babjo.whatdaybot.command;

import java.time.Clock;
import java.util.List;

import com.babjo.whatdaybot.repository.RisingKeywordRepository;
import com.babjo.whatdaybot.repository.RoomRepository;
import com.google.common.collect.ImmutableList;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;

public class CommandExecutor {

    private final List<Command> commands;

    public CommandExecutor(RoomRepository roomRepository,
                           RisingKeywordRepository risingKeywordRepository,
                           Clock clock) {
        commands = ImmutableList.of(new StartCommand(roomRepository),
                                    new StopCommand(roomRepository),
                                    new HateYouCommand(),
                                    new YesterdayCommand(clock),
                                    new TodayCommand(clock),
                                    new TomorrowCommand(clock),
                                    new DayAfterTomorrowCommand(clock),
                                    new MondaySongCommand(),
                                    new RisingKeywordsCommand(risingKeywordRepository),
                                    new AllRoomStateCommand(roomRepository));
    }

    public Message execute(MessageEvent<MessageContent> event) {
        for (Command command : commands) {
            if (command.matches(event)) {
                return command.execute(event);
            }
        }
        return null;
    }
}
