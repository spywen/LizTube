package com.liztube.exception;

import com.liztube.exception.exceptionType.PublicException;

import java.util.List;

/**
 * Created by maxime on 17/03/2015.
 */
public class UserException extends PublicException {

    public UserException(String log) {
        super(log);
    }

    public UserException(String log, List<String> messages) {
        super(log, messages);
    }

    public UserException(String log, String message) {
        super(log, message);
    }
}
