package com.babjo.whatdaybot;

import java.time.Clock;
import java.time.ZoneId;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.linecorp.bot.spring.boot.LineBotProperties;

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
                                 LineBotProperties properties) {
        return new BotService(roomRepository, Clock.system(ZoneId.of("UTC+09:00")), properties);
    }
}