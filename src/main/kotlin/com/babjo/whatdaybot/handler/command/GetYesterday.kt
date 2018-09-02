package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import java.time.Clock
import java.time.LocalDateTime

class GetYesterday(private val clock: Clock) : Command {
    override fun execute(): Message =
            TextMessage("어제는 ${LocalDateTime.now(clock).plusDays(-1).dayOfWeek.name} 입니다.")
}