package ru.spb.evelopers.util;

import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Утилиты для работы с датами.
 */
@Log4j
public final class DateUtil {

    /**
     * Получение текущего месяца.
     *
     * @return - текущий месяц в виде числа.
     */
    public static int getCurrentMonth() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.getMonthValue();
    }

    /**
     * Получение номера месяца из тектового представления даты.
     *
     * @param date - дата.
     * @return - номер месяца.
     */
    public static int getDateMonth(String date) {
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("dd.MM.yyyy")
                    .toFormatter();

            LocalDate localdate = LocalDate.parse(date, formatter);
            return localdate.getMonthValue();
        } catch (Exception e) {
            log.error("ERROR getDateMonth", e);
            e.printStackTrace();
        }
        return 0;
    }

}
