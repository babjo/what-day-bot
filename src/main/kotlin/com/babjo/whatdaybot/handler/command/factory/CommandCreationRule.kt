package com.babjo.whatdaybot.handler.command.factory

import com.babjo.whatdaybot.handler.command.Command
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent


interface CommandCreationRule {
    fun match(event: MessageEvent<MessageContent>): Boolean
    fun create(event: MessageEvent<MessageContent>): Command
}