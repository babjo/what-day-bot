package com.babjo.whatdaybot.repository;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import com.babjo.whatdaybot.utils.RisingKeywordCrawler;
import com.babjo.whatdaybot.utils.URLShortener;

public class CachedRisingKeywordRepository implements RisingKeywordRepository {

    private RisingKeywords latest;

    private final static Logger logger = LoggerFactory.getLogger(CachedRisingKeywordRepository.class);

    private final Clock clock;
    private final RisingKeywordCrawler crawler;
    private final URLShortener urlShortener;

    public CachedRisingKeywordRepository(
            ScheduledExecutorService executorService, RisingKeywordCrawler risingKeywordCrawler, Clock clock,
            URLShortener urlShortener) {
        this.clock = clock;
        this.crawler = risingKeywordCrawler;
        this.urlShortener = urlShortener;

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
                    LocalDateTime dateTime = LocalDateTime.now(clock);
                    int sec = dateTime.getSecond();
                    sec = sec % 30;
                    dateTime = dateTime.plusSeconds(-sec);

                    List<String> texts = crawler.doCrawling(dateTime);
                    latest = new RisingKeywords(dateTime,
                                                texts.stream()
                                                     .map(text -> new RisingKeyword(text, urlShortener
                                                             .shorten(String.format(
                                                                     "https://search.naver.com/search.naver?query=%s",
                                                                     encodeURIComponent(text)))))
                                                     .collect(toImmutableList()));

                    logger.info("Success refreshing: {}", latest);
                } catch (IOException e) {
                    logger.error("Fail to refresh", e);
                }
            }).get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Fail to refresh", e);
        }
    }

    public static String encodeURIComponent(String s) {
        String result;
        try {
            result = URLEncoder.encode(s, "UTF-8")
                               .replaceAll("\\+", "%20")
                               .replaceAll("\\%21", "!")
                               .replaceAll("\\%27", "'")
                               .replaceAll("\\%28", "(")
                               .replaceAll("\\%29", ")")
                               .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }
        return result;
    }

}
