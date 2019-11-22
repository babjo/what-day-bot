package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.TextMessage
import java.util.*

class GetRandomText(private val random: Random, private val candidateTexts: List<String>) : Command {
    override fun execute() =
        random.nextInt(candidateTexts.size)
            .let { candidateTexts[it] }
            .let { TextMessage(it) }
}
