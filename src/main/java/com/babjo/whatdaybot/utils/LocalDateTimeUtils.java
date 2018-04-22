package com.babjo.whatdaybot.utils;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class LocalDateTimeUtils {

    public static LocalDateTime getNextSalary(Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);

        LocalDateTime nextSalary = now.withDayOfMonth(25).withHour(0).withMinute(0).withSecond(0).withNano(0);
        if (now.getDayOfMonth() > 25) {
            nextSalary = nextSalary.plusMonths(1);
        }

        if (nextSalary.getDayOfWeek() == DayOfWeek.SATURDAY) {
            nextSalary = nextSalary.minusDays(2);
        } else if (nextSalary.getDayOfWeek() == DayOfWeek.SUNDAY) {
            nextSalary = nextSalary.minusDays(1);
        }

        return nextSalary;
    }
}
