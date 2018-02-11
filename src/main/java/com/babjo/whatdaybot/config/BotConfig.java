package com.babjo.whatdaybot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.babjo.whatdaybot.bot.WhatDayBot;
import com.babjo.whatdaybot.repository.RoomRepository;

@Configuration
public class BotConfig {

    @Bean
    public WhatDayBot whatDayBot(RoomRepository roomRepository) {
        return new WhatDayBot(roomRepository);
    }
}
