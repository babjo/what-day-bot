package com.babjo.whatdaybot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class WhatDayBotApplication

fun main(args: Array<String>) {
    SpringApplication.run(WhatDayBotApplication::class.java, *args)
}
