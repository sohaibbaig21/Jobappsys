package com.Jobapplicantsystem.Jobappsys.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Get current date
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    // Get current timestamp
    public static LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }

    // Format LocalDate → String
    public static String formatDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DATE_FORMAT);
    }

    // Format LocalDateTime → String
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_TIME_FORMAT);
    }

    // Parse String → LocalDate
    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMAT);
    }

    // Parse String → LocalDateTime
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMAT);
    }
}
