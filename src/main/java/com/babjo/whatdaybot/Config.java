package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

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
    public BotService botService(RoomRepository roomRepository,
                                 LineMessagingClient client) {
        return new BotService(Clock.system(ZoneId.of("UTC+09:00")), client, roomRepository, new Random());
    }
}