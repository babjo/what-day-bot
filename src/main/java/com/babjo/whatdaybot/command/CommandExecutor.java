package com.babjo.whatdaybot.command;

import java.time.Clock;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babjo.naver.client.PeriodicRisingKeywordCrawler;
import com.babjo.whatdaybot.repository.RoomRepository;
import com.google.common.collect.ImmutableList;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.VideoMessage;

public class CommandExecutor {

    private final List<Command> commands;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);

    public CommandExecutor(RoomRepository roomRepository,
                           PeriodicRisingKeywordCrawler periodicRisingKeywordCrawler,
                           Clock clock) {
        commands = ImmutableList.of(new StartCommand(roomRepository),
                                    new StopCommand(roomRepository),
                                    new HateYouCommand(),
                                    new YesterdayCommand(clock),
                                    new TodayCommand(clock),
                                    new TomorrowCommand(clock),
                                    new DayAfterTomorrowCommand(clock),
                                    new MondaySongCommand(),
                                    new RisingKeywordsCommand(periodicRisingKeywordCrawler),
                                    new AllRoomStateCommand(roomRepository),
                                    new SalaryCommand(clock),
        new Command() {
            @Override
            public Pattern getPattern() {
                LOGGER.info("getPattern");
                return Pattern.compile("아니");
            }

            @Override
            public Message execute(MessageEvent<MessageContent> event) {
                LOGGER.info("Image");
                return new VideoMessage("https://media.giphy.com/media/d3FyFzuFXikYruxy/giphy.gif", "https://media.giphy.com/media/d3FyFzuFXikYruxy/giphy.gif");
            }
        });
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
