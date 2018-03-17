package com.babjo.whatdaybot.utils;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RisingKeywordCrawler {

    private static final Logger logger = LoggerFactory.getLogger(RisingKeywordCrawler.class);

    public List<String> doCrawling(LocalDateTime dateTime) throws IOException {
        Document doc = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?datetime=" + dateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))).get();
        logger.info(doc.outerHtml());

        Element element = doc.select(".select_date").get(0);
        Elements keywords = element.select(".title");
        List<String> texts = keywords.stream().map(Element::text).collect(toImmutableList());
        logger.info("doCrawling results: " + String.join(",", texts));
        return texts;
    }
}
