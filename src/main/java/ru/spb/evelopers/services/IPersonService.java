package ru.spb.evelopers.services;


import ru.spb.evelopers.beans.PersonInfo;
import ru.spb.evelopers.core.exception.AppException;

import java.util.List;

public interface IPersonService {

    List<PersonInfo> findBirthdayByMonth(int month) throws AppException;

}
