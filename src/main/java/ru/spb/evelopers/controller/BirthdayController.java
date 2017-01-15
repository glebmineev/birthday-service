package ru.spb.evelopers.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spb.evelopers.beans.InfoBean;
import ru.spb.evelopers.beans.PersonInfo;
import ru.spb.evelopers.services.IPersonService;
import ru.spb.evelopers.task.MatchBirthdayTask;
import ru.spb.evelopers.util.DateUtil;
import ru.spb.evelopers.util.Messages;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Контроллер для работы с текстовым файлом,
 * содержащим список имен и дней рождений.
 */
@RestController
@RequestMapping(value = "/birthday")
@Log4j
public class BirthdayController {

    private String monthRegExp = "^(0?[1-9]|1[012])$";
    private String digitRegExp = "^[0-9]+$";
    private static AtomicInteger idSequence = new AtomicInteger();
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    private Map<Integer, Future<List<PersonInfo>>> tasks = new HashMap<>();

    @Autowired
    public IPersonService personService;

    /**
     * Поиск дней людей с днями рождения в указанном месяце.
     * @param month - номер месяца (1 - 12)
     * @return - id задачи.
     */
    @RequestMapping(
            value = "/findMatchBirthdays",
            method = RequestMethod.GET,
            produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String findMatchBirthdays(@RequestParam(value = "month", required = false) String month) {
        log.info("HTTP GET findMatchBirthdays" + (month == null ? "" : "?month=" + month));
        try {
            int monthDigits;
            if (month != null && !month.isEmpty()) {
                Pattern pattern = Pattern.compile(monthRegExp);
                Matcher matcher = pattern.matcher(month);
                if (matcher.matches()) {
                    monthDigits = Integer.valueOf(month);
                } else {
                    return Messages.validateWarningMessage;
                }
            } else {
                monthDigits = DateUtil.getCurrentMonth();
            }

            int id = idSequence.incrementAndGet();
            MatchBirthdayTask task =
                    new MatchBirthdayTask(personService, monthDigits);
            Future<List<PersonInfo>> submit = executor.submit(task);
            tasks.put(id, submit);

            return String.valueOf(id);
        } catch (Exception e) {
            log.info("ERROR", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Получение результата выполнения задачи.
     * @param taskId - id задачи.
     * @return - список людей.
     */
        @RequestMapping(value = "/checkBirthdaysTaskDone", method = RequestMethod.GET)
    public ResponseEntity checkBirthdaysTaskDone(@RequestParam("taskId") String taskId) {
        log.info("HTTP POST checkTaskDone with parameter task id" + (taskId == null ? "" : "?taskId=" + taskId));
        if (taskId != null && !taskId.isEmpty()) {
            Pattern pattern = Pattern.compile(digitRegExp);
            Matcher matcher = pattern.matcher(taskId);
            if (matcher.matches()) {
                Future<List<PersonInfo>> userInfoFuture = tasks.get(Integer.valueOf(taskId));
                if (userInfoFuture != null) {
                    if (userInfoFuture.isDone()) {
                        try {
                            return renderList(tasks.get(Integer.valueOf(taskId)).get());
                        } catch (Exception e) {
                            log.info("ERROR", e);
                            e.printStackTrace();
                        }
                    } else {
                        InfoBean infoBean = new InfoBean();
                        infoBean.setMessage(Messages.workingTaskMessage);
                        return infoMessageResponse(infoBean);
                    }
                }
            }
        }
        InfoBean infoBean = new InfoBean();
        infoBean.setMessage(Messages.notTaskidMessage);
        return infoMessageResponse(infoBean);
    }

    private ResponseEntity<InfoBean> infoMessageResponse(InfoBean data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private ResponseEntity<List<PersonInfo>> renderList(List<PersonInfo> data) {
        return data == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(data, HttpStatus.OK);
    }

}
