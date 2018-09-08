package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.model.Holiday
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import java.time.*

class GetNextSalaryDate(private val clock: Clock, private val holidays: List<Holiday> = emptyList()) : Command {
    override fun execute(): Message {
        val date = LocalDate.now(clock)
        val time = LocalTime.now(clock)

        val now = LocalDateTime.of(date, time)
        val nextSalary = LocalDateTime.of(getNextSalaryDate(date), LocalTime.MIN)

        var duration = Duration.between(now, nextSalary)
        val days = duration.toDays()
        duration = duration.minusDays(days)
        val hours = duration.toHours()
        duration = duration.minusHours(hours)
        val mins = duration.toMinutes()
        duration = duration.minusMinutes(mins)
        val secs = duration.toMillis() / 1000

        return TextMessage(String.format("다음 월급은 %d일 %d시간 %d분 %d초 남았습니다.", days, hours, mins, secs))
    }

    fun getNextSalaryDate(now: LocalDate): LocalDate {
        val salaryDateOfThisMonth = getSalaryDate(now.year, now.monthValue)
        return if (now.isEqual(salaryDateOfThisMonth) || now.isAfter(salaryDateOfThisMonth)) {
            getSalaryDateOfNextMonth(now)
        } else salaryDateOfThisMonth
    }

    fun getSalaryDate(year: Int, month: Int): LocalDate {
        var salaryDate = LocalDate.of(year, month, 25)
        while (!adjustDate(salaryDate).isEqual(salaryDate)) {
            salaryDate = adjustDate(salaryDate)
        }
        return salaryDate
    }

    private fun adjustDate(salaryDate: LocalDate): LocalDate = adjustWithHolidays(adjustWithWeekends(salaryDate))

    private fun adjustWithWeekends(salaryDate: LocalDate): LocalDate {
        return when (salaryDate.dayOfWeek) {
            DayOfWeek.SATURDAY -> salaryDate.minusDays(1)
            DayOfWeek.SUNDAY -> salaryDate.minusDays(2)
            else -> salaryDate
        }
    }

    private fun adjustWithHolidays(salaryDate: LocalDate): LocalDate {
        var adjusted: LocalDate = salaryDate
        for (holiday in holidays.reversed()) {
            if (adjusted.isEqual(holiday.date)) {
                adjusted = adjusted.minusDays(1)
            }
        }
        return adjusted
    }


    private fun getSalaryDateOfNextMonth(now: LocalDate): LocalDate {
        val nextMonth = now.plusMonths(1)
        return getSalaryDate(nextMonth.year, nextMonth.monthValue)
    }
}
