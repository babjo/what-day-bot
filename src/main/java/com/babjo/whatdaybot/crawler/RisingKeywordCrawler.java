package com.babjo.whatdaybot.crawler;

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

import com.babjo.whatdaybot.model.RisingKeyword;
import com.babjo.whatdaybot.utils.URIUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RisingKeywordCrawler {

    private static final Logger logger = LoggerFactory.getLogger(RisingKeywordCrawler.class);

    public List<RisingKeyword> doCrawling(LocalDateTime dateTime) throws IOException {
        Document doc = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?datetime=" + dateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))).get();
        logger.info(doc.outerHtml());

        Element element = doc.select(".select_date").get(0);
        Elements keywords = element.select(".title");

        List<RisingKeyword> result = keywords.stream()
                                             .map(Element::text)
                                             .map(text -> new RisingKeyword(text, String.format(
                                                     "https://search.naver.com/search.naver?query=%s",
                                                     URIUtils.encodeURIComponent(text))))
                                             .collect(toImmutableList());

        logger.info("doCrawling results: {}", result);

        return result;
    }
}
