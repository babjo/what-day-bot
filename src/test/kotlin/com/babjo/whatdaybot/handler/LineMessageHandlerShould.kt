package com.babjo.whatdaybot.handler


import com.babjo.whatdaybot.handler.rules.GetNextSalaryDate
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.source.UserSource
import com.linecorp.bot.model.message.TextMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Arrays.asList

@RunWith(MockitoJUnitRunner::class)
class LineMessageHandlerShould {

    private val handler = LineMessageHandler()

    @Test
    fun handleEventWhenAnEventComes() {
        handler.addHandlingRule("미워|미웡", "미워하지마")

        // GIVEN
        for (event in messageEvents("미워", "미웡")) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(TextMessage("미워하지마"))
        }


        handler.addHandlingRule("월급\\?|월급좀", GetNextSalaryDate(Clock.fixed(Instant.parse("2018-07-22T00:00:00.00Z"), ZoneId.of("UTC+00:00"))))
        for (event in messageEvents("월급?", "월급좀")) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(TextMessage("다음 월급은 3일 0시간 0분 0초 남았습니다."))
        }
    }

    @Test
    fun handleEventWhenAnUnSupportedEventComes() {
        handler.clearHandlingRules()

        // GIVEN
        for (event in messageEvents(null)) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(null)
        }
    }

    private fun messageEvents(vararg texts: String?) =
            asList(*texts).map { text -> MessageEvent<MessageContent>("token", UserSource("userId"), TextMessageContent("id", text), Instant.now()) }

}
