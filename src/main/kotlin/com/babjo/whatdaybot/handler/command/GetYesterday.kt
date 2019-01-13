package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.TextMessage
import java.time.Clock
import java.time.LocalDateTime

class GetYesterday(private val clock: Clock) : Command {
    override fun execute() =
        LocalDateTime
            .now(clock)
            .plusDays(-1)
            .dayOfWeek
            .name
            .let { TextMessage("어제는 $it 입니다.") }
}