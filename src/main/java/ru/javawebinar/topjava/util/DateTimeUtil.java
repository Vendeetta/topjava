package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalDateTime lt, LocalDateTime startTime, LocalDateTime endTime) {
        return !lt.toLocalDate().isBefore(startTime.toLocalDate()) &&
                !lt.toLocalTime().isBefore(startTime.toLocalTime()) &&
                lt.toLocalDate().isBefore(endTime.toLocalDate()) &&
                lt.toLocalTime().isBefore(endTime.toLocalTime());
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

