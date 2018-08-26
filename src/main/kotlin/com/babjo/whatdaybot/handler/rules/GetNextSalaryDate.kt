package com.babjo.whatdaybot.handler.rules

import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import java.time.*

class GetNextSalaryDate(private val clock: Clock) : LineMessageHandlingRule {

    override fun apply(event: MessageEvent<TextMessageContent>?): Message {
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
        val salaryDate = LocalDate.of(year, month, 25)
        return when (salaryDate.dayOfWeek) {
            DayOfWeek.SATURDAY -> salaryDate.minusDays(1)
            DayOfWeek.SUNDAY -> salaryDate.minusDays(2)
            else -> salaryDate
        }
    }


    private fun getSalaryDateOfNextMonth(now: LocalDate): LocalDate {
        val nextMonth = now.plusMonths(1)
        return getSalaryDate(nextMonth.year, nextMonth.monthValue)
    }
}
