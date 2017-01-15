package ru.spb.evelopers.services.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spb.evelopers.beans.PersonInfo;
import ru.spb.evelopers.core.exception.AppException;
import ru.spb.evelopers.dao.IPersonsDAO;
import ru.spb.evelopers.services.IPersonService;

import java.util.List;

/**
 * Created by gleb on 1/13/17.
 */

/**
 * Сервис для работы с текстовым файлом.
 */
@Component
@Log4j
public class PersonService implements IPersonService {

    @Autowired
    public IPersonsDAO personsDAO;

    @Override
    public List<PersonInfo> findBirthdayByMonth(int month) throws AppException {
        return personsDAO.findBirthdayByMonth(month);
    }

}
