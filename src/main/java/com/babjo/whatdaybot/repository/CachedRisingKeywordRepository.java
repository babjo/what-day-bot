package com.babjo.whatdaybot.repository;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babjo.whatdaybot.RisingKeywordCrawler;

public class CachedRisingKeywordRepository implements RisingKeywordRepository {

    private RisingKeywords latest;
    private final static Logger logger = LoggerFactory.getLogger(CachedRisingKeywordRepository.class);

    private final Clock clock;
    private final RisingKeywordCrawler crawler;

    public CachedRisingKeywordRepository(
            ScheduledExecutorService executorService, RisingKeywordCrawler risingKeywordCrawler, Clock clock) {

        this.clock = clock;
        this.crawler = risingKeywordCrawler;

        executorService.scheduleAtFixedRate(this::refresh, 1, 1, TimeUnit.MINUTES);

        // Init
        refresh();
    }

    @Override
    public RisingKeywords findLatestOne() {
        return latest;
    }

    private void refresh() {
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    latest = crawler.doCrawling(LocalDateTime.now(clock));
                } catch (IOException e) {
                    logger.error("Fail to refresh", e);
                }
            }).get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Fail to refresh", e);
        }
    }

}
