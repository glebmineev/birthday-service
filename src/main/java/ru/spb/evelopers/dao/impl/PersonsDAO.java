package ru.spb.evelopers.dao.impl;


import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ru.spb.evelopers.beans.*;
import ru.spb.evelopers.core.exception.AppException;
import ru.spb.evelopers.dao.IPersonsDAO;
import ru.spb.evelopers.util.BirthdayListProcessor;

import java.util.List;

/**
 * Created by gleb on 14.01.17.
 */

/**
 * Методы для работы с текстовым файлом.
 */

@Component
@Log4j
public class PersonsDAO implements IPersonsDAO {

    @Override
    public List<PersonInfo> findBirthdayByMonth(int month) throws AppException {
        return BirthdayListProcessor.parseBirthdayList(month);
    }

}
