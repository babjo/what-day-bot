package com.babjo.whatdaybot;

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

import com.babjo.whatdaybot.naver.openapi.URLShortenerClient;
import com.babjo.whatdaybot.crawler.PeriodicRisingKeywordCrawler;
import com.babjo.whatdaybot.crawler.RisingKeywordCrawler;
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
    public URLShortenerClient urlShortenerClient() {
        return new URLShortenerClient();
    }

    @Bean
    public PeriodicRisingKeywordCrawler periodicRisingKeywordCrawler(Clock clock,
                                                                     URLShortenerClient urlShortenerClient) {
        PeriodicRisingKeywordCrawler crawler = new PeriodicRisingKeywordCrawler(
                Executors.newScheduledThreadPool(1),
                new RisingKeywordCrawler(), clock, urlShortenerClient);

        crawler.start();
        return crawler;
    }
}