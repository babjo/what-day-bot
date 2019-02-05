package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.Holiday
import com.linecorp.bot.model.message.TextMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Arrays.asList

class GetNextSalaryDateShould {

    private val clock = Clock.fixed(Instant.parse("2018-07-22T00:00:00.00Z"), ZoneId.of("UTC+00:00"))
    private lateinit var command: GetNextSalaryDate

    @Before
    fun setUp() {
        command = GetNextSalaryDate(clock, asList(
                Holiday("Thanksgiving Day", LocalDate.of(2018, 9, 23)),
                Holiday("Thanksgiving Day", LocalDate.of(2018, 9, 24)),
                Holiday("Thanksgiving Day", LocalDate.of(2018, 9, 25)),
                Holiday("Thanksgiving Day", LocalDate.of(2018, 9, 26))
        ))
    }

    @Test
    fun execute() {
        assertThat(command.execute()).isEqualTo(TextMessage("다음 월급은 3일 0시간 0분 0초 남았습니다."))
    }

    @Test
    fun `should adjust the date, when the salary date is Saturday`() {
        // 2018. 08. 25. SATURDAY
        assertThat(command.getSalaryDate(2018, 8)).isEqualTo(LocalDate.of(2018, 8, 24))
    }

    @Test
    fun `should adjust the date, when the salary date is holiday`() {
        // 2018. 09. 25. Korean Thanksgiving Day
        assertThat(command.getSalaryDate(2018, 9)).isEqualTo(LocalDate.of(2018, 9, 21))
    }

    @Test
    fun `should adjust the date, when the salary date is weekends and holiday`() {
        // 2018. 08. 25. SATURDAY
        // 2018. 08. 20 ~ 2018. 08. 24. My day (Holidays)
        // 2018. 08. 18 ~ 2018. 08. 19. Weekend
        assertThat(GetNextSalaryDate(clock, (20..24).map {
            Holiday("My day", LocalDate.of(2018, 8, it))
        }).getSalaryDate(2018, 8)).isEqualTo(LocalDate.of(2018, 8, 17))
    }

    @Test
    fun `should not adjust the date, when the salary date is weekdays`() {
        // 2018. 07. 25. WEDNESDAY
        assertThat(command.getSalaryDate(2018, 7)).isEqualTo(LocalDate.of(2018, 7, 25))
    }

    @Test
    fun `should get the salary date Of this month, when the salary date of this month is not passed`() {
        assertThat(command.getNextSalaryDate(LocalDate.of(2018, 7, 13))).isEqualTo(LocalDate.of(2018, 7, 25))
    }

    @Test
    fun `should get the salary date of next month, when the salary date of this month is passed`() {
        assertThat(command.getNextSalaryDate(LocalDate.of(2018, 8, 24))).isEqualTo(LocalDate.of(2018, 9, 21))
    }


}