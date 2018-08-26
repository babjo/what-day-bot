package com.babjo.whatdaybot.handler.rules

import com.linecorp.bot.model.message.TextMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class GetNextSalaryDateShould {

    private val clock = Clock.fixed(Instant.parse("2018-07-22T00:00:00.00Z"), ZoneId.of("UTC+00:00"))
    private val rule = GetNextSalaryDate(clock)

    @Test
    fun apply() {
        assertThat(rule.apply(null)).isEqualTo(TextMessage("다음 월급은 3일 0시간 0분 0초 남았습니다."))
    }

    @Test
    fun adjustDateWhenSalaryDateIsSaturday() {
        // 2018. 08. 25. SATURDAY
        assertThat(rule.getSalaryDate(2018, 8)).isEqualTo(LocalDate.of(2018, 8, 24))
    }

    @Test
    fun notAdjustDateWhenSalaryDateIsWeekDays() {
        // 2018. 07. 25. WEDNESDAY
        assertThat(rule.getSalaryDate(2018, 7)).isEqualTo(LocalDate.of(2018, 7, 25))
    }

    @Test
    fun getSalaryDateOfThisMonthWhenSalaryDateOfThisMontIsNotPassed() {
        assertThat(rule.getNextSalaryDate(LocalDate.of(2018, 7, 13))).isEqualTo(LocalDate.of(2018, 7, 25))
    }

    @Test
    fun getSalaryDateOfNextMonthWhenSalaryDateOfThisMonthIsPassed() {
        assertThat(rule.getNextSalaryDate(LocalDate.of(2018, 8, 24))).isEqualTo(LocalDate.of(2018, 9, 25))
    }

}