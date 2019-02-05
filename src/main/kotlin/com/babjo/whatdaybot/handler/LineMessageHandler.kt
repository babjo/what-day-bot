package com.babjo.whatdaybot.handler

import com.babjo.whatdaybot.handler.command.Command
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import mu.KotlinLogging

@LineMessageHandler
class LineMessageHandler(val matcher: EventMatcher) {

    private val logger = KotlinLogging.logger {}

    @EventMapping
    fun handleEvent(event: MessageEvent<MessageContent>) =
        event.also { logger.info("Event comes. event: {}", it) }
            .let(matcher::match)
            ?.let(Command::execute)
            .also { logger.info("Reply: {}", it) }
}
