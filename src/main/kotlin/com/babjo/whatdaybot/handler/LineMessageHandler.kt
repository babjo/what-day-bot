package com.babjo.whatdaybot.handler

import com.babjo.whatdaybot.handler.command.factory.CommandFactory
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@LineMessageHandler
class LineMessageHandler(val factory: CommandFactory) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @EventMapping
    fun handleEvent(event: MessageEvent<MessageContent>): Message? {
        logger.info("Event comes. event: {}", event)
        val reply = factory.create(event)?.execute()
        logger.info("Reply: {}", reply)
        return reply
    }
}
