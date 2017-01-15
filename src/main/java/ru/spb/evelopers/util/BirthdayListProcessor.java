package ru.spb.evelopers.util;

import lombok.extern.log4j.Log4j;
import ru.spb.evelopers.beans.PersonInfo;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Обработчик файла с именами и днями рождениями.
 */
@Log4j
public class BirthdayListProcessor {

    private static URI personsURI;

    /**
     * Инициализация пути до файла.
     */
    static {
        try {
            String personsFilePath = "";
            URL appRes = BirthdayListProcessor.class.getResource("/application.properties");
            if (appRes != null) {
                File appPropFile = new File(appRes.getFile());
                if (appPropFile.exists()) {
                    InputStream is = new FileInputStream(appPropFile);
                    Properties property = new Properties();
                    property.load(is);
                    personsFilePath = property.getProperty("persons.file.path");
                }
            }

            File personsExternalFile = null;

            if (!personsFilePath.isEmpty()) {
                personsExternalFile = new File(personsFilePath);
            }

            if (personsExternalFile != null && personsExternalFile.exists()) {
                personsURI = personsExternalFile.toURI();
            } else {
                URL personsURL = BirthdayListProcessor.class.getResource("/persons.txt");
                if (personsURL != null) {
                    personsURI = personsURL.toURI();
                }
            }

        } catch (URISyntaxException e) {
            log.error("ERROR read file persons.txt", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("ERROR read file application.properties", e);
            e.printStackTrace();
        }
    }

    /**
     * Получение списка на основе текстового файла,
     * с фильтрацией по месяцу.
     * @param month - номер месяца
     *
     * @return - отфильтрованный список List<PersonInfo>.
     */
    public static List<PersonInfo> parseBirthdayList(int month) {
        List<PersonInfo> birthdayList = new ArrayList<>();

        if (personsURI != null) {

            try (Stream<String> stream = Files.lines(Paths.get(personsURI))) {
                birthdayList = stream.map(line -> line.split("\\|", 2))
                        .filter(array -> DateUtil.getDateMonth(array[0]) == month)
                        .map(array -> new PersonInfo(
                                array[1], array[0], calculateDaysBeforeBirthday(array[0])))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                log.error("ERROR parseBirthdayList", e);
                e.printStackTrace();
            }

        }

        return birthdayList;
    }

    /**
     * Расчет дней до дня рождения.
     * @param date - день родения.
     *
     * @return - число дней до дня роджения относительно текущей даты.
     */
    public static long calculateDaysBeforeBirthday(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateTime = LocalDate.parse(date, formatter);
            LocalDate currentDate = LocalDate.now();
            LocalDate birthdayDate = LocalDate.of(
                    currentDate.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
            if (birthdayDate.isBefore(currentDate)) {
                birthdayDate = LocalDate.of(
                        currentDate.getYear() + 1, dateTime.getMonthValue(), dateTime.getDayOfMonth());
            }
            long between = ChronoUnit.DAYS.between(currentDate, birthdayDate);
            return between;
        } catch (Exception e) {
            log.error("ERROR calculateDaysBeforeBirthday", e);
            e.printStackTrace();
        }
        return 0;
    }

}
