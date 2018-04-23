package com.babjo.whatdaybot.utils;

import java.time.LocalDateTime;

public class SalaryDateUtils {

    public static LocalDateTime getNextSalary(LocalDateTime now) {
        return adjustWeekend(calcNextSalary(now));
    }

    private static LocalDateTime calcNextSalary(LocalDateTime now) {
        if (now.getDayOfMonth() >= 25) {
            now = now.plusMonths(1);
        }

        return now.withDayOfMonth(25).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private static LocalDateTime adjustWeekend(LocalDateTime nextSalary) {
        switch (nextSalary.getDayOfWeek()) {
            case SATURDAY:
                return nextSalary.minusDays(2);
            case SUNDAY:
                return nextSalary.minusDays(1);
            default:
                return nextSalary;
        }
    }

}
