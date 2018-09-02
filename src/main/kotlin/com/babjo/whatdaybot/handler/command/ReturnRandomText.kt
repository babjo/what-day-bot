package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import java.util.*

class ReturnRandomText(private val random: Random, private val candidateTexts: List<String>) : Command {
    override fun execute(): Message = TextMessage(candidateTexts[random.nextInt(candidateTexts.size)])
}