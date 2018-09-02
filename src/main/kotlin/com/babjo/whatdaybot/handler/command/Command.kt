package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.Message

@FunctionalInterface
interface Command {
    fun execute(): Message
}