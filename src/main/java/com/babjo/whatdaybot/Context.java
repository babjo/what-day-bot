package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.babjo.whatdaybot.command.CommandExecutor;
import com.babjo.whatdaybot.naver.PeriodicRisingKeywordCrawler;
import com.babjo.whatdaybot.naver.RisingKeywordCrawler;
import com.babjo.whatdaybot.naver.URLShortener;
import com.babjo.whatdaybot.repository.RoomRepository;
import com.babjo.whatdaybot.service.BotService;
import com.zaxxer.hikari.HikariDataSource;

import com.linecorp.bot.client.LineMessagingClient;

@Configuration
@EnableScheduling
public class Context {

    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public BotService botService(Clock clock, RoomRepository roomRepository,
                                 PeriodicRisingKeywordCrawler periodicRisingKeywordCrawler,
                                 LineMessagingClient client) {
        return new BotService(client, roomRepository,
                              new CommandExecutor(roomRepository, periodicRisingKeywordCrawler, clock),
                              new Random());
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("UTC+09:00"));
    }

    @Bean
    @ConfigurationProperties(prefix = "naver.openapi")
    public URLShortener urlShortener() {
        return new URLShortener();
    }

    @Bean
    public PeriodicRisingKeywordCrawler periodicRisingKeywordCrawler(Clock clock, URLShortener urlUtils) {
        PeriodicRisingKeywordCrawler crawler = new PeriodicRisingKeywordCrawler(
                Executors.newScheduledThreadPool(1),
                new RisingKeywordCrawler(), clock, urlUtils);

        crawler.start();
        return crawler;
    }
}