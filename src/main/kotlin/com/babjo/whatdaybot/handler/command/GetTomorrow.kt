package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import java.time.Clock
import java.time.LocalDateTime

class GetTomorrow(private val clock: Clock) : Command {
    override fun execute(): Message =
            TextMessage("내일은 ${LocalDateTime.now(clock).plusDays(1).dayOfWeek.name} 입니다.")
}