package com.babjo.whatdaybot

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class ControllerShould {
    @Test
    fun returnOK_WhenServerIsHealthy() {
        assertThat(HealthCheckController().`do`(), `is`("OK"))
    }
}