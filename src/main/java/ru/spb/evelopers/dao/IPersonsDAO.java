package ru.spb.evelopers.dao;

import ru.spb.evelopers.beans.PersonInfo;
import ru.spb.evelopers.core.exception.AppException;

import java.util.List;

/**
 * Created by gleb on 1/13/17.
 */
public interface IPersonsDAO {

    List<PersonInfo> findBirthdayByMonth(int month) throws AppException;

}
