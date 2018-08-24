package com.babjo.whatdaybot.utils;

import java.time.LocalDate;

public class SalaryDateUtils {

    public static LocalDate getSalaryDate(int year, int month) {
        LocalDate salaryDate = LocalDate.of(year, month, 25);
        switch (salaryDate.getDayOfWeek()) {
            case SATURDAY:
                return salaryDate.minusDays(1);
            case SUNDAY:
                return salaryDate.minusDays(2);
            default:
                return salaryDate;
        }
    }

    public static LocalDate getNextSalaryDate(LocalDate now) {
        LocalDate salaryDateOfThisMonth = getSalaryDate(now.getYear(), now.getMonthValue());
        if (now.isEqual(salaryDateOfThisMonth) || now.isAfter(salaryDateOfThisMonth)) {
            return getSalaryDateOfNextMonth(now);
        }
        return salaryDateOfThisMonth;
    }

    private static LocalDate getSalaryDateOfNextMonth(LocalDate now) {
        LocalDate nextMonth = now.plusMonths(1);
        return getSalaryDate(nextMonth.getYear(), nextMonth.getMonthValue());
    }
}
