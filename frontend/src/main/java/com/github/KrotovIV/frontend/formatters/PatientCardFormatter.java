package com.github.KrotovIV.frontend.formatters;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class PatientCardFormatter {

    public String formatAge(int age) {
        int lastDigit = age % 10;
        int lastTwoDigits = age % 100;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            return age + " лет";
        }

        switch (lastDigit) {
            case 1: return age + " год";
            case 4: return age + " года";
            default: return age + " лет";
        }
    }

    public String formatRelativeDate(LocalDate date) {
        LocalDate now = LocalDate.now();
        Period period = Period.between(date, now);

        if (period.getYears() > 0) {
            return period.getYears() + " " + formatYears(period.getYears());
        } else if (period.getMonths() > 0) {
            return period.getMonths() + " " + formatMonths(period.getMonths());
        } else if (period.getDays() >= 7) {
            return period.getDays() / 7 + " " + formatWeeks(period.getDays() / 7);
        } else if (period.getDays() > 0) {
            return period.getDays() + " " + formatDays(period.getDays());
        } else {
            return "сегодня";
        }
    }

    private String formatWeeks(int weeks) {
        return weeks == 1 ? "неделю назад" : "недели назад";
    }

    private String formatYears(int years) {
        return years == 1 ? "год назад" : (years < 5 ? "года назад" : "лет назад");
    }

    private String formatMonths(int months) {
        return months == 1 ? "месяц назад" : (months < 5 ? "месяца назад" : "месяцев назад");
    }

    private String formatDays(int days) {
        return days == 1 ? "день назад" : (days < 5 ? "дня назад" : "дней назад");
    }
}
