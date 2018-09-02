package com.babjo.whatdaybot.handler


import com.babjo.whatdaybot.handler.command.GetNextSalaryDate
import com.babjo.whatdaybot.handler.command.ReturnSimpleText
import com.babjo.whatdaybot.handler.command.TurnOffPushMessages
import com.babjo.whatdaybot.handler.command.factory.CommandFactory
import com.babjo.whatdaybot.handler.command.factory.TextPatternRule
import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.source.UserSource
import com.linecorp.bot.model.message.TextMessage
import com.nhaarman.mockitokotlin2.mock
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

    private val factory = CommandFactory()
    private val handler = LineMessageHandler(factory)

    @Test
    fun handleEventWhenAnEventComes_1() {
        // GIVEN
        factory.addCreationRule(TextPatternRule("미워|미웡") { ReturnSimpleText("미워하지마") })
        for (event in messageEvents("미워", "미웡")) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(TextMessage("미워하지마"))
        }
    }

    @Test
    fun handleEventWhenAnEventComes_2() {
        // GIVEN
        factory.addCreationRule(TextPatternRule("월급\\?|월급좀") { GetNextSalaryDate(Clock.fixed(Instant.parse("2018-07-22T00:00:00.00Z"), ZoneId.of("UTC+00:00"))) })

        for (event in messageEvents("월급?", "월급좀")) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(TextMessage("다음 월급은 3일 0시간 0분 0초 남았습니다."))
        }
    }

    @Test
    fun handleEventWhenAnEventComes_3() {
        // GIVEN
        val roomRepo = mock<RoomRepository>()
        factory.addCreationRule(TextPatternRule("stop") { TurnOffPushMessages(it, roomRepo) })

        // WHEN, THENl
        assertThat(handler.handleEvent(messageEvent("stop"))).isEqualTo(TextMessage("OK! STOP!"))
    }


    @Test
    fun handleEventWhenAnUnSupportedEventComes() {
        factory.clearCreationRules()

        // GIVEN
        for (event in messageEvents(null)) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(null)
        }
    }

    private fun messageEvents(vararg texts: String?) =
            asList(*texts).map { text -> messageEvent(text) }

    private fun messageEvent(text: String?) =
            MessageEvent<MessageContent>("token", UserSource("userId"), TextMessageContent("id", text), Instant.now())

}
