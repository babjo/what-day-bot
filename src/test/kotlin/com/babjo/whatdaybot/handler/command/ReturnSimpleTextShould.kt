package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.TextMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ReturnSimpleTextShould {
    private val command = ReturnSimpleText("Hi")

    @Test
    fun execute() {
        assertThat(command.execute()).isEqualTo(TextMessage("Hi"))
    }
}