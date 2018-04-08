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
import com.babjo.whatdaybot.repository.CachedRisingKeywordRepository;
import com.babjo.whatdaybot.repository.RisingKeywordRepository;
import com.babjo.whatdaybot.repository.RoomRepository;
import com.babjo.whatdaybot.service.BotService;
import com.babjo.whatdaybot.utils.RisingKeywordUtils;
import com.babjo.whatdaybot.utils.URLUtils;
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
                                 RisingKeywordRepository risingKeywordRepository,
                                 LineMessagingClient client) {
        return new BotService(client, roomRepository,
                              new CommandExecutor(roomRepository, risingKeywordRepository, clock),
                              new Random());
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("UTC+09:00"));
    }

    @Bean
    @ConfigurationProperties(prefix = "naver.openapi")
    public URLUtils urlUtils() {
        return new URLUtils();
    }

    @Bean
    public RisingKeywordRepository risingKeywordRepository(Clock clock, URLUtils urlUtils) {
        return new CachedRisingKeywordRepository(Executors.newScheduledThreadPool(1),
                                                 new RisingKeywordUtils(), clock, urlUtils);
    }
}