package com.babjo.whatdaybot.context;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.babjo.naver.client.PeriodicRisingKeywordCrawler;
import com.babjo.naver.client.RisingKeywordCrawler;
import com.babjo.naver.client.URLShortenerApi;
import com.babjo.whatdaybot.command.CommandExecutor;
import com.babjo.whatdaybot.repository.RoomRepository;
import com.babjo.whatdaybot.storage.BotService;
import com.zaxxer.hikari.HikariDataSource;

import com.linecorp.bot.client.LineMessagingClient;

@Configuration
@EnableScheduling
public class Context {

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
    public URLShortenerApi urlShortenerClient() {
        return new URLShortenerApi();
    }

    @Bean
    public PeriodicRisingKeywordCrawler periodicRisingKeywordCrawler(Clock clock,
                                                                     URLShortenerApi urlShortenerClient) {
        PeriodicRisingKeywordCrawler crawler = new PeriodicRisingKeywordCrawler(
                Executors.newScheduledThreadPool(1),
                new RisingKeywordCrawler(), clock, urlShortenerClient);

        crawler.start();
        return crawler;
    }
}