package com.babjo.whatdaybot.command;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.babjo.whatdaybot.model.RisingKeyword;
import com.babjo.whatdaybot.naver.PeriodicRisingKeywordCrawler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RisingKeywordsCommand implements Command {

    private final Pattern pattern = Pattern.compile("핫해");
    private final PeriodicRisingKeywordCrawler periodicRisingKeywordCrawler;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        List<RisingKeyword> keywords = periodicRisingKeywordCrawler.getLatestRisingKeywords();
        LocalDateTime time = periodicRisingKeywordCrawler.getLatestRefreshTime();
        return new TextMessage(
                String.format("인기검색어 %s\n", time) +
                String.join("\n",
                            IntStream.range(0, 10)
                                     .mapToObj(i -> String
                                             .format("%d. %s: %s",
                                                     i + 1,
                                                     keywords.get(i).getText(),
                                                     keywords.get(i).getUrl()))
                                     .collect(toImmutableList()))
        );
    }
}
