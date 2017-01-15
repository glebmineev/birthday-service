package ru.spb.evelopers.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spb.evelopers.beans.ErrorData;
import ru.spb.evelopers.beans.InfoBean;
import ru.spb.evelopers.beans.PersonInfo;
import ru.spb.evelopers.services.IPersonService;
import ru.spb.evelopers.task.MatchBirthdayTask;
import ru.spb.evelopers.util.DateUtil;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gleb on 1/12/17.
 */
@RestController
@RequestMapping(value = "/birthday")
@Log4j
public class BirthdayController {

    private String monthRegExp = "^(0?[1-9]|1[012])$";
    private static AtomicInteger idSequence = new AtomicInteger();
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    private Map<Integer, Future<List<PersonInfo>>> tasks = new HashMap<>();

    @Autowired
    public IPersonService personService;

    @RequestMapping(
            value = "/findMatchBirthdays",
            method = RequestMethod.GET)
    @ResponseBody
    public String findMatchBirthdays(@RequestParam(value = "month", required = false) String month) {
        log.info("HTTP GET findMatchBirthdays" + (month == null ? "" : "?month=" + month));
        try {
            int id = idSequence.incrementAndGet();

            if (month != null) {
                int monthDigits;

                Pattern pattern = Pattern.compile(monthRegExp);
                Matcher matcher = pattern.matcher(month);

                if (matcher.matches()) {
                    monthDigits = Integer.valueOf(month);
                } else {
                    monthDigits = DateUtil.getCurrentMonth();
                }

                MatchBirthdayTask task =
                        new MatchBirthdayTask(personService, monthDigits);
                Future<List<PersonInfo>> submit = executor.submit(task);
                tasks.put(id, submit);
            }

            return String.valueOf(id);
        } catch (Exception e) {
            log.info("ERROR", e);
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/checkTask", method = RequestMethod.POST)
    public ResponseEntity checkTask(@RequestParam("taskId") String taskId) {
        log.info("HTTP POST checkTask with parameter task id" + (taskId == null ? "" : "?taskId=" + taskId));
        if (taskId != null) {
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
                    infoBean.setMessage("Задача выполняется. Повторите запрос позже.");
                    return infoMessageResponse(infoBean);
                }
            }
        }
        InfoBean infoBean = new InfoBean();
        infoBean.setMessage("Задачи с таким идентификатором нет.");
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
