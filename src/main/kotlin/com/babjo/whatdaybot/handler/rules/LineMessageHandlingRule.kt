package com.babjo.whatdaybot.handler.rules

import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.message.Message


@FunctionalInterface
interface LineMessageHandlingRule {
    fun apply(event: MessageEvent<TextMessageContent>?): Message
}