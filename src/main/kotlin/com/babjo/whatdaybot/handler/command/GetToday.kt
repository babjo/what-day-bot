package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import java.time.Clock
import java.time.LocalDateTime

class GetToday(private val clock: Clock) : Command {
    override fun execute(): Message =
            TextMessage("오늘은 ${LocalDateTime.now(clock).dayOfWeek.name} 입니다.")
}