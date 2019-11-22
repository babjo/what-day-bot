package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.TextMessage

class ReturnSimpleText(private val text: String) : Command {
    override fun execute() = TextMessage(text)
}
