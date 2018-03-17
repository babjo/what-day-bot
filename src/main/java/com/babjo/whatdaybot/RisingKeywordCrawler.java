package com.babjo.whatdaybot;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babjo.whatdaybot.repository.RisingKeywords;

public class RisingKeywordCrawler {

    private static final Logger logger = LoggerFactory.getLogger(RisingKeywordCrawler.class);

    public RisingKeywords doCrawling(LocalDateTime dateTime) throws IOException {
        int sec = dateTime.getSecond();
        sec = sec % 30;
        dateTime = dateTime.plusSeconds(-sec);

        Document doc = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?datetime=" + dateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))).get();
        logger.info(doc.outerHtml());

        Element element = doc.select(".select_date").get(0);
        Elements keywords = element.select(".title");
        RisingKeywords risingKeywords = new RisingKeywords(dateTime, keywords.stream().map(Element::text)
                                                                             .collect(toImmutableList()));
        logger.info(risingKeywords.toString());

        return risingKeywords;
    }
}
