package ru.spb.evelopers.beans;

import lombok.Data;


@Data
public class PersonInfo {

    //Имя
    private String name;
    //День рождения
    private String birthday;
    //Сколько дней осталось до дня рождения
    private long daysBeforeBirthday;

    public PersonInfo(String name, String birthday, long daysBeforeBirthday) {
        this.name = name;
        this.birthday = birthday;
        this.daysBeforeBirthday = daysBeforeBirthday;
    }

}
