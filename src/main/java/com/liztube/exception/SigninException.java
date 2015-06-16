package com.liztube.exception;

import com.liztube.exception.exceptionType.PublicException;

import java.util.List;

/**
 * Created by laurent on 15/02/15.
 */
public class SigninException extends PublicException {

    public SigninException(String log) {
        super(log);
    }

    public SigninException(String log, List<String> messages) {
        super(log, messages);
    }

    public SigninException(String log, String message) {
        super(log, message);
    }
}
