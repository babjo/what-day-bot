package com.babjo.whatdaybot.handler


import com.babjo.whatdaybot.Room
import com.babjo.whatdaybot.handler.command.GetNextSalaryDate
import com.babjo.whatdaybot.handler.command.ReturnSimpleText
import com.babjo.whatdaybot.handler.command.TurnOffPushMessages
import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.source.UserSource
import com.linecorp.bot.model.message.TextMessage
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Arrays.asList

class LineMessageHandlerShould {

    @Test
    fun `should handle the event, when an event comes 1`() {
        // GIVEN
        val matcher = eventMatcher {
            pattern("미워|미웡") { ReturnSimpleText("미워하지마") }
        }
        val handler = LineMessageHandler(matcher)

        for (event in messageEvents("미워", "미웡")) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(TextMessage("미워하지마"))
        }
    }

    @Test
    fun `should handle the event, when an event comes 2`() {
        // GIVEN
        val matcher = eventMatcher {
            pattern("월급\\?|월급좀") {
                GetNextSalaryDate(Clock.fixed(Instant.parse("2018-07-22T00:00:00.00Z"), ZoneId.of("UTC+00:00")))
            }
        }
        val handler = LineMessageHandler(matcher)

        for (event in messageEvents("월급?", "월급좀")) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(TextMessage("다음 월급은 3일 0시간 0분 0초 남았습니다."))
        }
    }

    @Test
    fun `should handle the event, when an event comes 3`() {
        // GIVEN
        val roomRepo = mockk<RoomRepository>()
        every {
            roomRepo.save(any<Room>())
        } returns Room("id", false)

        val matcher = eventMatcher {
            pattern("stop") { TurnOffPushMessages(it, roomRepo) }
        }
        val handler = LineMessageHandler(matcher)


        // WHEN, THENl
        assertThat(handler.handleEvent(messageEvent("stop"))).isEqualTo(TextMessage("OK! STOP!"))
    }


    @Test
    fun `should handle the event, when an UnSupportedEvent comes`() {
        // GIVEN
        val matcher = eventMatcher {  }
        val handler = LineMessageHandler(matcher)

        for (event in messageEvents(null)) {
            // WHEN, THEN
            assertThat(handler.handleEvent(event)).isEqualTo(null)
        }
    }

    private fun messageEvents(vararg texts: String?) =
        asList(*texts).map { text -> messageEvent(text) }

    private fun messageEvent(text: String?) =
        MessageEvent<MessageContent>(
            "token",
            UserSource("userId"),
            TextMessageContent("id", text),
            Instant.now()
        )

}
