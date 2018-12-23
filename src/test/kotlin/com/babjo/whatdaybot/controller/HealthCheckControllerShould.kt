package com.babjo.whatdaybot.controller

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class HealthCheckControllerShould {
    @Test
    fun returnOK_WhenServerIsHealthy() {
        assertThat(HealthCheckController().`do`(), `is`("OK"))
    }
}