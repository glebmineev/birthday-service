package ru.spb.evelopers.beans;

import lombok.Data;

/**
 * Created by gleb on 1/12/17.
 */
@Data
public class PersonInfo {

    private String name;
    private String birthday;
    private long daysBeforeBirthday;

    public PersonInfo(String name, String birthday, long daysBeforeBirthday) {
        this.name = name;
        this.birthday = birthday;
        this.daysBeforeBirthday = daysBeforeBirthday;
    }

}
