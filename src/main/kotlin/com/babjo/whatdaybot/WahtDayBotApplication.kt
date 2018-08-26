package com.babjo.whatdaybot

import com.babjo.whatdaybot.service.BotService
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
@LineMessageHandler
open class WhatDayBotApplication {

    private val logger = LoggerFactory.getLogger(WhatDayBotApplication::class.java)
    @Autowired
    private val botService: BotService? = null


    @Scheduled(cron = "0 30 09 ? * MON", zone = "Asia/Seoul")
    fun pushMondayMessage() {
        botService!!.pushMondayMessage()
    }

    @Scheduled(cron = "0 30 09 ? * MON-FRI", zone = "Asia/Seoul")
    fun pushTodayOfWeekMessages() {
        botService!!.pushTodayOfWeekMessage()
    }

    @Scheduled(cron = "0 0 19 ? * MON-FRI", zone = "Asia/Seoul")
    fun pushOverworkQuestionMessages() {
        botService!!.pushWorkLateAtNightMessage()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(WhatDayBotApplication::class.java, *args)
}
