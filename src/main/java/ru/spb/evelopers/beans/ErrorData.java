package ru.spb.evelopers.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by gleb on 14.01.17.
 */

@Data
@NoArgsConstructor
public class ErrorData {

    /**
     * Код ошибки
     */
    private String code;

    /**
     * Сообщение ошибки
     */
    private String message;

}
