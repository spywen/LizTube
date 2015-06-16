package com.liztube.exception;

import com.liztube.exception.exceptionType.InternalException;
import com.liztube.exception.exceptionType.PublicException;
import com.liztube.utils.EnumError;

import java.util.List;

/**
 * User not found exception : if a user is not authenticated according to their login informations
 */
public class UserNotFoundException extends PublicException {

    public UserNotFoundException(String log) {
        super(log);
    }

    public UserNotFoundException(String log, String message) {
        super(log, message);
    }

    public UserNotFoundException(String log, List<String> messages) {
        super(log, messages);
    }
}
