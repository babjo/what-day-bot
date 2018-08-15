package com.babjo.whatdaybot.utils;

import static com.babjo.whatdaybot.utils.SalaryDateUtils.getNextSalaryDate;
import static com.babjo.whatdaybot.utils.SalaryDateUtils.getSalaryDate;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

public class SalaryDateUtilsTest {

    @Test
    public void Should_AdjustDate_WhenSalaryDateIsSaturday() {
        // 2018. 08. 25. SATURDAY
        assertThat(getSalaryDate(2018, 8)).isEqualTo(LocalDate.of(2018, 8, 24));
    }

    @Test
    public void Should_NotAdjustDate_WhenSalaryDateIsWeekDays() {
        // 2018. 07. 25. WEDNESDAY
        assertThat(getSalaryDate(2018, 7)).isEqualTo(LocalDate.of(2018, 7, 25));
    }

    @Test
    public void Should_GetSalaryDateOfThisMonth_WhenSalaryDateOfThisMontIsNotPassed() {
        assertThat(getNextSalaryDate(LocalDate.of(2018, 7, 13))).isEqualTo(LocalDate.of(2018, 7, 25));
    }

    @Test
    public void Should_GetSalaryDateOfNextMonth_WhenSalaryDateOfThisMonthIsPassed() {
        assertThat(getNextSalaryDate(LocalDate.of(2018, 7, 26))).isEqualTo(LocalDate.of(2018, 8, 24));
    }
}
