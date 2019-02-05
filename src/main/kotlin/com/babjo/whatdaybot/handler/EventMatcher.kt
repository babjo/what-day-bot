package com.babjo.whatdaybot.handler

import com.babjo.whatdaybot.handler.command.Command
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import java.util.regex.Pattern

class EventMatcher {
    private val rules = mutableListOf<Rule>()

    fun match(event: MessageEvent<MessageContent>) =
        rules
            .firstOrNull { it.matchFunc(event) }
            ?.let { it.createFunc(event) }

    fun pattern(regex: String, createFun: (event: MessageEvent<MessageContent>) -> Command) {
        val compiled = Pattern.compile(regex)
        val matchFun = { event: MessageEvent<MessageContent> ->
            val message = event.message
            when (message) {
                is TextMessageContent -> compiled.matcher(message.text).matches()
                else -> false
            }
        }
        rules.add(Rule(matchFun, createFun))
    }
}

fun eventMatcher(init: EventMatcher.() -> Unit): EventMatcher {
    val matcher = EventMatcher()
    matcher.init()
    return matcher
}

data class Rule(
    val matchFunc: (MessageEvent<MessageContent>) -> Boolean,
    val createFunc: (MessageEvent<MessageContent>) -> Command
)
