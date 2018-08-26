package com.babjo.whatdaybot.handler

import com.babjo.whatdaybot.handler.rules.LineMessageHandlingRule
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import java.util.regex.Pattern

@LineMessageHandler
class LineMessageHandler {

    private val rules: MutableList<Pair<Pattern, LineMessageHandlingRule>> = ArrayList()

    @EventMapping
    fun handleEvent(event: MessageEvent<MessageContent>): Message? {
        val message = event.message
        if (message is TextMessageContent)
            for (rule in rules) {
                if (rule.first.matcher(message.text).matches()) {
                    return rule.second.apply(MessageEvent(event.replyToken, event.source, message, event.timestamp))
                }
            }
        return null
    }


    fun addHandlingRule(textPattern: String, replyText: String) {
        this.addHandlingRule(textPattern, object : LineMessageHandlingRule {
            override fun apply(event: MessageEvent<TextMessageContent>?): Message {
                return TextMessage(replyText)
            }
        })
    }

    fun addHandlingRule(textPattern: String, rule: LineMessageHandlingRule) {
        this.rules.add(Pair<Pattern, LineMessageHandlingRule>(Pattern.compile(textPattern), rule))
    }


    fun clearHandlingRules() {
        this.rules.clear()
    }

}
