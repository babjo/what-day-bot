package com.babjo.whatdaybot.command;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.babjo.whatdaybot.model.RisingKeywords;
import com.babjo.whatdaybot.repository.RisingKeywordRepository;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RisingKeywordsCommand implements Command {

    private final Pattern pattern = Pattern.compile("핫해");
    private final RisingKeywordRepository risingKeywordRepository;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        RisingKeywords risingKeywords = risingKeywordRepository.findLatestRisingKeywords();
        return new TextMessage(
                String.format("인기검색어 %s\n", risingKeywords.getTime()) +
                String.join("\n",
                            IntStream.range(0, 10)
                                     .mapToObj(i -> String
                                             .format("%d. %s: %s",
                                                     i + 1,
                                                     risingKeywords.getKeywords().get(i).getText(),
                                                     risingKeywords.getKeywords().get(i).getUrl()))
                                     .collect(toImmutableList()))
        );
    }
}
