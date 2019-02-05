package com.babjo.whatdaybot

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class ControllerShould {
    @Test
    fun `should return OK, when the server is healthy`() {
        assertThat(HealthCheckController().`do`(), `is`("OK"))
    }
}