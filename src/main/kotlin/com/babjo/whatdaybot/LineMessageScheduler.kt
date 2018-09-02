package com.babjo.whatdaybot

import com.babjo.whatdaybot.handler.command.GetMondaySong
import com.babjo.whatdaybot.handler.command.GetToday
import com.babjo.whatdaybot.handler.command.ReturnRandomText
import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.Message
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import java.time.Clock
import java.util.*
import java.util.Arrays.asList
import java.util.concurrent.ExecutionException


class LineMessageScheduler(private val clock: Clock,
                           private val random: Random,
                           private val client: LineMessagingClient,
                           private val roomRepository: RoomRepository) {

    private val logger = LoggerFactory.getLogger(LineMessageScheduler::class.java)

    @Scheduled(cron = "0 30 09 ? * MON", zone = "Asia/Seoul")
    fun pushMondayMessage() {
        pushMessage(GetMondaySong().execute())
        pushMessage(GetToday(clock).execute())
    }

    @Scheduled(cron = "0 30 09 ? * MON-FRI", zone = "Asia/Seoul")
    fun pushTodayOfWeekMessages() {
        pushMessage(GetToday(clock).execute())
    }

    @Scheduled(cron = "0 0 19 ? * MON-FRI", zone = "Asia/Seoul")
    fun pushOverworkQuestionMessages() {
        pushMessage(ReturnRandomText(random, asList("오늘 야근?", "야근야근???", "오늘도 야근?!???", "야근 ㄱ?", "야근각?")).execute())
    }

    private fun pushMessage(message: Message?) {
        val rooms = roomRepository.findAll()
        rooms.stream().filter { it.botRunning }.forEach { room ->
            val pushMessage = PushMessage(room.id, message)
            try {
                client.pushMessage(pushMessage).get()
            } catch (e: InterruptedException) {
                logger.error("Failed to push a message. roomId: {}", room.id, e)
            } catch (e: ExecutionException) {
                logger.error("Failed to push a message. roomId: {}", room.id, e)
            }
        }
    }
}