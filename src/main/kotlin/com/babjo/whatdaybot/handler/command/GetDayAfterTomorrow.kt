package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.TextMessage
import java.time.Clock
import java.time.LocalDateTime

class GetDayAfterTomorrow(val clock: Clock) : Command {
    override fun execute() =
        LocalDateTime
            .now(clock)
            .plusDays(2)
            .dayOfWeek
            .name
            .let { TextMessage("모레는 $it 입니다.") }
}
