package com.babjo.whatdaybot.crawler;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babjo.whatdaybot.model.RisingKeyword;
import com.babjo.whatdaybot.naver.openapi.URLShortenerClient;

public class PeriodicRisingKeywordCrawler {

    private final static Logger logger = LoggerFactory.getLogger(PeriodicRisingKeywordCrawler.class);

    private final Clock clock;
    private final ScheduledExecutorService executorService;
    private final RisingKeywordCrawler risingKeywordCrawler;
    private final URLShortenerClient urlShortenerClient;

    private LocalDateTime latestRefreshTime;
    private List<RisingKeyword> latestRisingKeywords;

    public PeriodicRisingKeywordCrawler(
            ScheduledExecutorService executorService,
            RisingKeywordCrawler risingKeywordCrawler,
            Clock clock,
            URLShortenerClient urlShortenerClient) {
        this.executorService = executorService;
        this.clock = clock;
        this.risingKeywordCrawler = risingKeywordCrawler;
        this.urlShortenerClient = urlShortenerClient;
    }

    public void start() {
        executorService.scheduleAtFixedRate(this::refresh, 0, 60, TimeUnit.SECONDS);
    }

    private void refresh() {
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    LocalDateTime dateTime = LocalDateTime.now(clock);
                    int sec = dateTime.getSecond();
                    sec = sec % 30;
                    dateTime = dateTime.plusSeconds(-sec);

                    List<RisingKeyword> keywords = risingKeywordCrawler.doCrawling(dateTime);

                    latestRefreshTime = dateTime;
                    latestRisingKeywords = keywords.stream().map(
                            keyword -> new RisingKeyword(keyword.getText(), urlShortenerClient
                                    .shorten(keyword.getUrl()))).collect(toImmutableList());

                    logger.info("Success refreshing: {}", latestRisingKeywords);
                } catch (IOException e) {
                    logger.error("Fail to refresh", e);
                }
            }).get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Fail to refresh", e);
        }
    }

    public LocalDateTime getLatestRefreshTime() {
        return latestRefreshTime;
    }

    public List<RisingKeyword> getLatestRisingKeywords() {
        return latestRisingKeywords;
    }
}
