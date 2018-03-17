package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.babjo.whatdaybot.repository.CachedRisingKeywordRepository;
import com.babjo.whatdaybot.repository.RisingKeywordRepository;
import com.babjo.whatdaybot.repository.RoomRepository;

import com.linecorp.bot.client.LineMessagingClient;

@Configuration
@EnableScheduling
public class Config {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public BotService botService(Clock clock, RoomRepository roomRepository,
                                 RisingKeywordRepository risingKeywordRepository,
                                 LineMessagingClient client) {
        return new BotService(clock, client, roomRepository, risingKeywordRepository, new Random());
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("UTC+09:00"));
    }

    @Bean
    public RisingKeywordRepository risingKeywordRepository(Clock clock) {
        return new CachedRisingKeywordRepository(Executors.newScheduledThreadPool(1),
                                                 new RisingKeywordCrawler(), clock);
    }
}