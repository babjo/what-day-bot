package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.TextMessage
import java.time.Clock
import java.time.LocalDateTime

class GetToday(private val clock: Clock) : Command {
    override fun execute() =
        LocalDateTime
            .now(clock)
            .dayOfWeek
            .name
            .let { TextMessage("오늘은 $it 입니다.") }
}
