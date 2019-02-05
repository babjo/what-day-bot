package com.babjo.whatdaybot.handler.command.factory

import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent

class CommandFactory {

    private val rules = mutableListOf<CommandCreationRule>()
    fun create(event: MessageEvent<MessageContent>) =
        rules
            .firstOrNull { it.match(event) }
            .let { it?.create(event) }

    fun addCreationRule(creationRule: CommandCreationRule) = this.apply { rules.add(creationRule) }
    fun clearCreationRules() = this.apply { rules.clear() }
}