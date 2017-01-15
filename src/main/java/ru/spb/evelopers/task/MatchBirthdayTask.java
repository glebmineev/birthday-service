package ru.spb.evelopers.task;

import lombok.extern.log4j.Log4j;
import ru.spb.evelopers.beans.PersonInfo;
import ru.spb.evelopers.core.exception.AppException;
import ru.spb.evelopers.services.IPersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Асинхронная задача, ищет людей,
 * празднующих ДР в указанном месяце.
 */
@Log4j
public class MatchBirthdayTask implements Callable<List<PersonInfo>> {

    private IPersonService personService;
    private int month;

    private Random random = new Random();
    private int min = 60000;
    private int max = 120000;

    public MatchBirthdayTask(IPersonService personService, int month) {
        this.personService = personService;
        this.month = month;
    }

    @Override
    public List<PersonInfo> call() throws Exception {
        log.debug("start task : " + Thread.currentThread().getName());
        int delay = random.nextInt(max - min) + min;
        log.debug("task : " + Thread.currentThread().getName() + " was paused of" + delay);
        TimeUnit.MILLISECONDS.sleep(delay);
        List<PersonInfo> birthdayByMonth = new ArrayList<>();
        try {
            birthdayByMonth = personService.findBirthdayByMonth(month);
        } catch (AppException e) {
            log.error("ERROR", e);
            e.printStackTrace();
        }
        log.debug("end task : " + Thread.currentThread().getName());
        return birthdayByMonth;
    }

}
