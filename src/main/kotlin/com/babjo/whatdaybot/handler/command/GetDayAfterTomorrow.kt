package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import java.time.Clock
import java.time.LocalDateTime

class GetDayAfterTomorrow(val clock: Clock) : Command {
    override fun execute(): Message =
            TextMessage("모레는 ${LocalDateTime.now(clock).plusDays(2).dayOfWeek.name} 입니다.")
}