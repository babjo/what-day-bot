package com.babjo.whatdaybot

import com.babjo.whatdaybot.handler.command.GetMondaySong
import com.babjo.whatdaybot.handler.command.GetRandomText
import com.babjo.whatdaybot.handler.command.GetToday
import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.Message
import io.reactivex.rxkotlin.toCompletable
import io.reactivex.rxkotlin.toObservable
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import java.time.Clock
import java.time.LocalDate
import java.util.*
import java.util.Arrays.asList


class LineMessageScheduler(
    private val clock: Clock,
    private val random: Random,
    private val client: LineMessagingClient,
    private val roomRepository: RoomRepository,
    private val holidays: List<Holiday>
) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(cron = "0 30 09 ? * MON", zone = "Asia/Seoul")
    fun pushMondaySongMessage() {
        GetMondaySong()
            .execute()
            .also(this::pushMessage)
    }

    @Scheduled(cron = "0 30 09 ? * MON-FRI", zone = "Asia/Seoul")
    fun pushTodayOfWeekMessages() {
        GetToday(clock)
            .execute()
            .also(this::pushMessage)
    }

    @Scheduled(cron = "0 0 19 ? * MON-FRI", zone = "Asia/Seoul")
    fun pushOverworkQuestionMessages() {
        GetRandomText(random, asList("오늘 야근?", "야근야근???", "오늘도 야근?!???", "야근 ㄱ?", "야근각?"))
            .execute()
            .also(this::pushMessage)
    }

    private fun pushMessage(message: Message?) {
        roomRepository.findAll()
            .toObservable()
            .filter { it.botRunning }
            .filter {
                val isHoliday = isHoliday()
                if (isHoliday) {
                    logger.info("Filtered a message to push: message=$message, room=$it")
                }
                !isHoliday
            }
            .map { PushMessage(it.id, message) }
            .flatMapCompletable { client.pushMessage(it).toCompletable() }
            .doOnError { logger.error("Failed to push a message", it) }
            .subscribe()
    }

    fun isHoliday() =
        holidays
            .map { it.date }
            .contains(LocalDate.now(clock))
}