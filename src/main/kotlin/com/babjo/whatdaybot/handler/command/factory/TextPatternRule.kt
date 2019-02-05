package com.babjo.whatdaybot.handler.command.factory

import com.babjo.whatdaybot.handler.command.Command
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import java.util.regex.Pattern

class TextPatternRule(
    private val pattern: Pattern,
    private val createFun: (event: MessageEvent<MessageContent>) -> Command
) : CommandCreationRule {

    override fun match(event: MessageEvent<MessageContent>) =
        event.message.let {
            when (it) {
                is TextMessageContent -> pattern.matcher(it.text).matches()
                else -> false
            }
        }

    override fun create(event: MessageEvent<MessageContent>) = createFun(event)

    constructor(
        textPattern: String,
        createFun: (event: MessageEvent<MessageContent>) -> Command
    ) : this(Pattern.compile(textPattern), createFun)
}
