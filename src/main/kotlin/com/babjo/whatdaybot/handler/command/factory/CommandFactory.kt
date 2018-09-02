package com.babjo.whatdaybot.handler.command.factory

import com.babjo.whatdaybot.handler.command.Command
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent

class CommandFactory {

    private val rules = mutableListOf<CommandCreationRule>()

    fun create(event: MessageEvent<MessageContent>): Command? {
        for (rule: CommandCreationRule in rules) {
            if (rule.match(event)) {
                return rule.create(event)
            }
        }
        return null
    }

    fun addCreationRule(creationRule: CommandCreationRule) {
        rules.add(creationRule)
    }

    fun clearCreationRules(){
        rules.clear()
    }
}